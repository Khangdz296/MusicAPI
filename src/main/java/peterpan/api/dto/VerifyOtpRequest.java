package peterpan.api.dto;

import lombok.Data; 

@Data
public class VerifyOtpRequest {
    private String username;
    private String otpCode;
}