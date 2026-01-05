package peterpan.api.controller;



import peterpan.api.dto.VerifyOtpRequest;
import peterpan.api.model.User;
import peterpan.api.repository.UserRepository;
import peterpan.api.controller.SendGridEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SendGridEmailService sendGridEmailService;

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi mã hóa mật khẩu", e);
        }
    }

    private String generateRandomOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println("Đăng ký user: " + user.getUsername() + " - " + user.getEmail());

        if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            System.out.println("Username/Email đã tồn tại");
            return new ResponseEntity<>(
                    Map.of("status", "error", "message", "Username hoặc Email đã tồn tại."),
                    HttpStatus.BAD_REQUEST);
        }

        user.setPassword(hashPassword(user.getPassword()));
        user.setSessionKey(UUID.randomUUID().toString());

        String newOtp = generateRandomOtp();
        user.setOtpCode(newOtp);
        user.setIsActive(false);

        User savedUser = userRepository.save(user);
        System.out.println("Đã lưu user vào database, ID: " + savedUser.getId());
        try {
            System.out.println("Đang gửi OTP qua SendGrid...");
            sendGridEmailService.sendOtpEmailHtml(savedUser.getEmail(), newOtp);

            return new ResponseEntity<>(
                    Map.of("status", "success",
                            "message", "Đăng ký thành công! OTP đã được gửi đến email của bạn.",
                            "user_id", savedUser.getId()),
                    HttpStatus.CREATED);

        } catch (Exception e) {
            System.err.println("Lỗi gửi email SendGrid: " + e.getMessage());
            e.printStackTrace();

            // Vẫn trả về thông báo thành công nhưng có warning
            return new ResponseEntity<>(
                    Map.of("status", "warning",
                            "message", "Đăng ký thành công nhưng không thể gửi email. Vui lòng dùng /generate-otp.",
                            "user_id", savedUser.getId(),
                            "test_otp", newOtp), // Để test nếu email fail
                    HttpStatus.CREATED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginDetails) {
        String username = loginDetails.get("username");
        String rawPassword = loginDetails.get("password");
        String hashedPassword = hashPassword(rawPassword);

        Optional<User> userOpt = userRepository.findByUsernameAndPassword(username, hashedPassword);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (user.getIsActive() == false) {
                return new ResponseEntity<>(
                        Map.of("status", "error", "message", "Tài khoản chưa được kích hoạt OTP."),
                        HttpStatus.FORBIDDEN);
            }

            String newSessionKey = UUID.randomUUID().toString();
            user.setSessionKey(newSessionKey);
            userRepository.save(user);

            return new ResponseEntity<>(
                    Map.of("status", "success", "message", "Đăng nhập thành công!",
                            "user_id", user.getId(), "session_key", newSessionKey),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    Map.of("status", "error", "message", "Sai tên đăng nhập hoặc mật khẩu."),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader(name = "X-Session-Key") String sessionKey) {
        if (sessionKey == null || sessionKey.isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("status", "error", "message", "Thiếu Session Key. Vui lòng đăng nhập trước."),
                    HttpStatus.FORBIDDEN);
        }

        Optional<User> userOpt = userRepository.findBySessionKey(sessionKey);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Map<String, Object> userData = new HashMap<>();
            userData.put("user_id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("email", user.getEmail());
            userData.put("full_name", user.getFullName());
            userData.put("join_date", user.getJoinDate());
            userData.put("is_active", user.getIsActive());

            return new ResponseEntity<>(
                    Map.of("status", "success", "user", userData),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    Map.of("status", "error", "message", "Session Key không hợp lệ hoặc đã hết hạn."),
                    HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateOtp(@RequestBody Map<String, String> request) {
        String username = request.get("username");

        if (username == null || username.isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("status", "error", "message", "Vui lòng cung cấp Username."),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("status", "error", "message", "Người dùng không tồn tại."),
                    HttpStatus.NOT_FOUND);
        }

        User user = userOpt.get();

        if (user.getIsActive()) {
            return new ResponseEntity<>(
                    Map.of("status", "success", "message", "Tài khoản đã được kích hoạt."),
                    HttpStatus.OK);
        }

        String newOtp = generateRandomOtp();
        user.setOtpCode(newOtp);
        userRepository.save(user);

        // Gửi OTP qua SendGrid
        try {
            sendGridEmailService.sendOtpEmailHtml(user.getEmail(), newOtp);

            return new ResponseEntity<>(
                    Map.of("status", "success",
                            "message", "Mã OTP mới đã được gửi đến email."),
                    HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("Lỗi gửi email: " + e.getMessage());
            return new ResponseEntity<>(
                    Map.of("status", "error",
                            "message", "Không thể gửi email. Vui lòng thử lại sau.",
                            "test_otp", newOtp), // Debug
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        String username = request.getUsername();
        String otpCode = request.getOtpCode();

        if (username == null || otpCode == null) {
            return new ResponseEntity<>(
                    Map.of("status", "error", "message", "Thiếu username/email hoặc mã OTP."),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("status", "error", "message", "Người dùng không tồn tại."),
                    HttpStatus.NOT_FOUND);
        }

        User user = userOpt.get();

        if (otpCode.equals(user.getOtpCode())) {
            user.setIsActive(true);
            user.setOtpCode(null);
            userRepository.save(user);

            return new ResponseEntity<>(
                    Map.of("status", "success", "message", "Xác thực OTP thành công! Tài khoản đã được kích hoạt."),
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>(
                    Map.of("status", "error", "message", "Mã OTP không đúng. Vui lòng thử lại."),
                    HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/test-sendgrid")
    public ResponseEntity<?> testSendGrid() {
        try {
            boolean success = sendGridEmailService.testSendGridConnection();
            if (success) {
                return ResponseEntity.ok(
                        Map.of("status", "success",
                                "message", "SendGrid kết nối thành công! Kiểm tra email của bạn.")
                );
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("status", "error",
                                "message", "SendGrid kết nối thất bại! Kiểm tra API key và sender email."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error",
                            "message", "Lỗi: " + e.getMessage()));
        }
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestHeader(name = "X-Session-Key") String sessionKey,
            @RequestBody Map<String, String> passwordData) {

        if (sessionKey == null || sessionKey.isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("status", "error", "message", "Thiếu Session Key"),
                    HttpStatus.FORBIDDEN);
        }

        Optional<User> userOpt = userRepository.findBySessionKey(sessionKey);

        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("status", "error", "message", "Session không hợp lệ"),
                    HttpStatus.FORBIDDEN);
        }

        User user = userOpt.get();

        String oldPassword = passwordData.get("old_password");
        String newPassword = passwordData.get("new_password");

        if (oldPassword == null || newPassword == null ||
                oldPassword.isEmpty() || newPassword.isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("status", "error", "message", "Vui lòng nhập đầy đủ thông tin"),
                    HttpStatus.BAD_REQUEST);
        }

        String hashedOldPassword = hashPassword(oldPassword);

        if (!hashedOldPassword.equals(user.getPassword())) {
            return new ResponseEntity<>(
                    Map.of("status", "error", "message", "Mật khẩu cũ không đúng"),
                    HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(hashPassword(newPassword));
        userRepository.save(user);

        return new ResponseEntity<>(
                Map.of("status", "success", "message", "Đổi mật khẩu thành công"),
                HttpStatus.OK);
    }
}