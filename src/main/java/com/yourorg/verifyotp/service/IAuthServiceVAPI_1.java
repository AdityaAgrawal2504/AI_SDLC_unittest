package com.yourorg.verifyotp.service;

import com.yourorg.verifyotp.dto.VerifyOtpRequestVAPI_1;
import com.yourorg.verifyotp.dto.VerifyOtpSuccessResponseVAPI_1;
import com.yourorg.verifyotp.exception.CustomApiExceptionVAPI_1;

public interface IAuthServiceVAPI_1 {
    /**
     * Verifies an OTP and returns session tokens upon success.
     */
    VerifyOtpSuccessResponseVAPI_1 verifyOtp(VerifyOtpRequestVAPI_1 request) throws CustomApiExceptionVAPI_1;
}
```
```java
// Service Implementation: AuthServiceVAPI_1.java