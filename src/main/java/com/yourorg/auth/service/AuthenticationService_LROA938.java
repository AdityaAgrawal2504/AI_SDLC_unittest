package com.yourorg.auth.service;

import com.yourorg.auth.dto.request.LoginRequest_LROA938;
import com.yourorg.auth.dto.response.LoginSuccessResponse_LROA938;

/**
 * Interface defining the business logic for user authentication.
 */
public interface AuthenticationService_LROA938 {

    /**
     * Authenticates a user with phone and password, then triggers an OTP for 2FA.
     * @param request The login request containing credentials.
     * @return A response containing a success message and a session token for OTP verification.
     */
    LoginSuccessResponse_LROA938 loginAndRequestOtp(LoginRequest_LROA938 request);
}
```
```java
// src/main/java/com/yourorg/auth/service/impl/AuthenticationServiceImpl_LROA938.java