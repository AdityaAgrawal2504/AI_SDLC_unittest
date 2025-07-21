package com.verifyotpapi.service;

import com.verifyotpapi.dto.request.VerifyOtpRequest_VOTP1;
import com.verifyotpapi.dto.response.VerifyOtpSuccessResponse_VOTP1;

/**
 * Interface for authentication-related business logic.
 */
public interface IAuthService_VOTP1 {

    /**
     * Verifies a user-submitted OTP and returns session tokens upon success.
     */
    VerifyOtpSuccessResponse_VOTP1 verifyOtp(VerifyOtpRequest_VOTP1 request);
}
```
```java
// src/main/java/com/verifyotpapi/service/AuthService_VOTP1.java