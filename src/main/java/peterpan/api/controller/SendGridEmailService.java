package peterpan.api.controller;


import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridEmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    @Value("${sendgrid.from.name}")
    private String fromName;

    public void sendOtpEmail(String toEmail, String otpCode) {
        Email from = new Email(fromEmail, fromName);
        Email to = new Email(toEmail);
        String subject = "Mã OTP Xác Thực Tài Khoản";

        String textContent = "Xin chào,\n\n" +
                "Mã OTP của bạn là: " + otpCode + "\n\n" +
                "Mã này có hiệu lực trong 5 phút.\n\n" +
                "Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email.\n\n" +
                "Trân trọng,\n";

        Content content = new Content("text/plain", textContent);
        Mail mail = new Mail(from, subject, to, content);

        sendEmail(mail, toEmail);
    }

    public void sendOtpEmailHtml(String toEmail, String otpCode) {
        Email from = new Email(fromEmail, fromName);
        Email to = new Email(toEmail);
        String subject = "Mã OTP Xác Thực Tài Khoản - Music";

        String htmlContent = buildOtpHtmlTemplate(otpCode);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, to, content);

        sendEmail(mail, toEmail);
    }
    private void sendEmail(Mail mail, String toEmail) {
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("SendGrid: Email OTP đã gửi thành công đến: " + toEmail);
                System.out.println("   Status Code: " + response.getStatusCode());
            } else {
                System.err.println("SendGrid: Gửi email với status code: " + response.getStatusCode());
                System.err.println("Response Body: " + response.getBody());
            }

        } catch (IOException e) {
            System.err.println("SendGrid Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể gửi email qua SendGrid", e);
        }
    }

    private String buildOtpHtmlTemplate(String otpCode) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "  <meta charset='UTF-8'>" +
                "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "  <title>Mã OTP</title>" +
                "</head>" +
                "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
                "  <table width='100%' cellpadding='0' cellspacing='0' style='background-color: #f4f4f4; padding: 20px;'>" +
                "    <tr>" +
                "      <td align='center'>" +
                "        <table width='600' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>" +
                "          <tr>" +
                "            <td style='padding: 40px 30px; text-align: center;'>" +
                "              <h1 style='color: #333; margin: 0 0 20px 0; font-size: 28px;'>Xác Thực Tài Khoản</h1>" +
                "              <p style='color: #666; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;'>" +
                "                Xin chào,<br>Để hoàn tất đăng ký, vui lòng sử dụng mã OTP bên dưới:" +
                "              </p>" +
                "              <div style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 30px; text-align: center; border-radius: 8px; margin: 0 0 30px 0;'>" +
                "                <span style='font-size: 40px; font-weight: bold; letter-spacing: 10px; color: #ffffff; font-family: monospace;'>" +
                otpCode +
                "                </span>" +
                "              </div>" +
                "              <div style='background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 0 0 30px 0; text-align: left;'>" +
                "                <p style='color: #856404; font-size: 14px; margin: 0;'>" +
                "                  <strong>Lưu ý:</strong> Mã này có hiệu lực trong <strong style='color: #e74c3c;'>5 phút</strong>." +
                "                </p>" +
                "              </div>" +
                "              <p style='color: #999; font-size: 13px; line-height: 1.5; margin: 0 0 10px 0;'>" +
                "                Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này." +
                "              </p>" +
                "              <hr style='border: none; border-top: 1px solid #eee; margin: 30px 0;'>" +
                "              <p style='color: #999; font-size: 12px; line-height: 1.5; margin: 0;'>" +
                "                Trân trọng" +
                "              </p>" +
                "            </td>" +
                "          </tr>" +
                "        </table>" +
                "        <p style='color: #999; font-size: 11px; margin: 20px 0 0 0; text-align: center;'>" +
                "          Email này được gửi tự động, vui lòng không trả lời." +
                "        </p>" +
                "      </td>" +
                "    </tr>" +
                "  </table>" +
                "</body>" +
                "</html>";
    }
    public boolean testSendGridConnection() {
        try {
            Email from = new Email(fromEmail, fromName);
            Email to = new Email(fromEmail); // Gửi cho chính mình
            String subject = "Test SendGrid Connection";
            Content content = new Content("text/plain",
                    "Nếu bạn nhận được email này, SendGrid đã hoạt động thành công!");

            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("Test SendGrid thành công! Status: " + response.getStatusCode());
                return true;
            } else {
                System.err.println("Test SendGrid thất bại! Status: " + response.getStatusCode());
                System.err.println("Response: " + response.getBody());
                return false;
            }

        } catch (Exception e) {
            System.err.println("Test SendGrid error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
