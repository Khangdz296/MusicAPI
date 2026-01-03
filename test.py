import unittest
import requests
import json
import random
import string
import sys # Cần dùng sys để thoát nếu người dùng không nhập OTP

class APITestCase(unittest.TestCase):
    
    BASE_URL = "http://localhost:8080/api"
    
    RANDOM_SUFFIX = ''.join(random.choices(string.ascii_letters + string.digits, k=8))
    TEST_USERNAME = f"py_test_{RANDOM_SUFFIX}"
    TEST_PASSWORD = "password123"
    TEST_EMAIL = f"py_test_{RANDOM_SUFFIX}@example.com"
    
    session_key = ""

    # ... (Hàm test_01_register KHÔNG THAY ĐỔI) ...
    def test_01_register(self):
        print("\n--- 1. TEST REGISTER ---")
        
        register_payload = {
            "username": self.TEST_USERNAME,
            "password": self.TEST_PASSWORD,
            "email": self.TEST_EMAIL,
            "fullName": "Tester Nguyen",
            "phone": "0912345678" 
        }
        
        try:
            response = requests.post(f"{self.BASE_URL}/register", json=register_payload)
            self.assertEqual(response.status_code, 201, f"Lỗi Register: Status code {response.status_code}. Response: {response.text}")
            
            data = response.json()
            self.assertEqual(data.get("status"), "success", "Đăng ký thất bại: Status không phải 'success'")
            print(f"  [Register SUCCESS] User ID: {data.get('user_id')}")
            print(f"  *** LƯU Ý: Username dùng cho test OTP là: {self.TEST_USERNAME} ***")

        except requests.exceptions.ConnectionError:
            self.fail("Không thể kết nối đến server. Đảm bảo Spring Boot đang chạy ở cổng 8080.")
    
    # --- HÀM TEST VERIFY OTP ĐÃ CẬP NHẬT ĐỂ TỰ NHẬP OTP ---
    def test_02_verify_otp(self):
        """
        Test API Kiểm tra OTP (yêu cầu nhập thủ công).
        """
        print("\n--- 2. TEST VERIFY OTP (NHẬP THỦ CÔNG) ---")
        
        # *** DÙNG input() ĐỂ YÊU CẦU BRO NHẬP VÀO ***
        user_input_otp = input(f"   [NHẬP OTP] Vui lòng nhập mã OTP cho user '{self.TEST_USERNAME}' (Enter để bỏ qua): ")
        
        if not user_input_otp:
            print("   [BỎ QUA] Không nhập OTP, bỏ qua bước xác thực và các bước tiếp theo.")
            self.skipTest("Bỏ qua do không nhập OTP.")
            return # Thoát khỏi hàm test này
        
        print(f"   [ĐANG TEST] Sử dụng mã OTP: {user_input_otp}")

        verify_otp_payload = {
            "username": self.TEST_USERNAME,
            "otpCode": user_input_otp.strip() # Lấy OTP người dùng nhập
        }
        
        response = requests.post(f"{self.BASE_URL}/verify-otp", json=verify_otp_payload)
        
        self.assertEqual(response.status_code, 200, f"Lỗi Verify OTP: Status code {response.status_code}. Response: {response.text}")
        
        data = response.json()
        self.assertEqual(data.get("status"), "success", "Xác thực OTP thất bại: Status không phải 'success'")
        
        print(f"  [Verify OTP SUCCESS] Tài khoản {self.TEST_USERNAME} đã được kích hoạt.")
    # ------------------------------------------------------------------------

    # ... (Hàm test_03_login và test_04_profile KHÔNG THAY ĐỔI) ...
    def test_03_login(self):
        print("\n--- 3. TEST LOGIN ---")
        # ... nội dung test login ...
        login_payload = {
            "username": self.TEST_USERNAME,
            "password": self.TEST_PASSWORD
        }
        response = requests.post(f"{self.BASE_URL}/login", json=login_payload)
        self.assertEqual(response.status_code, 200, f"Lỗi Login sau khi kích hoạt: Status code {response.status_code}. Response: {response.text}")
        data = response.json()
        self.assertEqual(data.get("status"), "success", "Đăng nhập thất bại: Status không phải 'success'")
        APITestCase.session_key = data.get("session_key")
        self.assertIsNotNone(APITestCase.session_key, "Đăng nhập thất bại: Không nhận được session_key")
        print(f"  [Login SUCCESS] Session Key đã lưu: {APITestCase.session_key[:10]}...")

    def test_04_profile(self):
        print("\n--- 4. TEST PROFILE ---")
        if not self.session_key:
            self.fail("Chưa có Session Key, vui lòng kiểm tra lại hàm test_03_login.")
        profile_headers = {
            "X-Session-Key": self.session_key
        }
        response = requests.get(f"{self.BASE_URL}/profile", headers=profile_headers)
        self.assertEqual(response.status_code, 200, f"Lỗi Profile: Status code {response.status_code}. Response: {response.text}")
        data = response.json()
        self.assertEqual(data.get("status"), "success", "Lấy Profile thất bại: Status không phải 'success'")
        user_info = data.get("user")
        self.assertEqual(user_info.get("username"), self.TEST_USERNAME, "Username trong Profile không khớp")
        print(f"  [Profile SUCCESS] Username: {user_info.get('username')}")

        
if __name__ == '__main__':
    unittest.main()