package com.verifyotpapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the request body for the /v1/auth/verify-otp endpoint.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpRequest_VOTP1 {

    @NotBlank(message = "verificationToken is required")
    private String verificationToken;

    @NotBlank(message = "otpCode is required")
    @Size(min = 6, max = 6, message = "otpCode must be exactly 6 digits")
    @Pattern(regexp = "\\d{6}", message = "otpCode must be a 6-digit numeric string")
    private String otpCode;
}
```
```java
// src/main/java/com/verifyotpapi/dto/response/VerifyOtpSuccessResponse_VOTP1.java