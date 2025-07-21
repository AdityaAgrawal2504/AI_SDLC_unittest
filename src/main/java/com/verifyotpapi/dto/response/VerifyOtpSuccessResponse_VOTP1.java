package com.verifyotpapi.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the successful response body for the OTP verification endpoint.
 */
@Data
@Builder
public class VerifyOtpSuccessResponse_VOTP1 {
    private String accessToken;
    private String tokenType = "Bearer";
    private long expiresIn;
    private String refreshToken;
}
```
```java
// src/main/java/com/verifyotpapi/dto/response/ApiErrorResponse_VOTP1.java