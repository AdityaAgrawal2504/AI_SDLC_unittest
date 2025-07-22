package com.example.api.auth.service;

import com.example.api.auth.dto.LoginRequestLCVAPI_1;
import com.example.api.auth.dto.LoginSuccessResponseLCVAPI_1;

/**
 * Interface defining the contract for the authentication service.
 */
public interface IAuthenticationServiceLCVAPI_1 {

    /**
     * Handles the business logic for user credential validation and triggers the OTP service.
     * @param request The login request containing user credentials.
     * @return A success response upon successful OTP dispatch.
     */
    LoginSuccessResponseLCVAPI_1 loginWithCredentials(LoginRequestLCVAPI_1 request);
}
```

src/main/java/com/example/api/auth/service/AuthenticationServiceLCVAPI_1.java
```java