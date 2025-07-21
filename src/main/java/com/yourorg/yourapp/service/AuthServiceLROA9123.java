package com.yourorg.yourapp.service;

import com.yourorg.yourapp.dto.request.LoginRequestLROA9123;
import com.yourorg.yourapp.dto.response.LoginSuccessResponseLROA9123;

/**
 * Interface defining the contract for authentication-related business logic.
 */
public interface AuthServiceLROA9123 {

    /**
     * Validates user credentials and initiates the OTP sending process.
     * @param request The login request containing phone and password.
     * @return A response containing the OTP session token for the next step.
     */
    LoginSuccessResponseLROA9123 loginAndRequestOtp(LoginRequestLROA9123 request);
}
```
src/main/java/com/yourorg/yourapp/service/AuthServiceImplLROA9123.java