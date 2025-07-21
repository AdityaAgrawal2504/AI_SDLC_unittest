package com.yourorg.verifyotp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class VerifyOtpRequestVAPI_1 {

    @NotBlank(message = "Verification token cannot be blank.")
    private String verificationToken;

    @NotBlank(message = "OTP code cannot be blank.")
    @Size(min = 6, max = 6, message = "OTP code must be exactly 6 digits.")
    @Pattern(regexp = "\\d{6}", message = "OTP code must contain only digits.")
    private String otpCode;

    // Getters and Setters
    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
}
```
```java
// DTO: VerifyOtpSuccessResponseVAPI_1.java