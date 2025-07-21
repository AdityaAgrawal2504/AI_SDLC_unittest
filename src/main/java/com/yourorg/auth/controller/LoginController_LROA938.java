package com.yourorg.auth.controller;

import com.yourorg.auth.dto.request.LoginRequest_LROA938;
import com.yourorg.auth.dto.response.LoginSuccessResponse_LROA938;
import com.yourorg.auth.service.AuthenticationService_LROA938;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling the authentication endpoint for login and OTP request.
 */
@RestController
@RequestMapping("/v1/auth")
public class LoginController_LROA938 {

    private static final Logger logger = LogManager.getLogger(LoginController_LROA938.class);
    private final AuthenticationService_LROA938 authenticationService;

    public LoginController_LROA938(AuthenticationService_LROA938 authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Handles the POST request to /v1/auth/login.
     * Validates credentials and sends an OTP as the second factor of authentication.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginSuccessResponse_LROA938> loginAndRequestOtp(
            @Valid @RequestBody LoginRequest_LROA938 loginRequest) {
        long startTime = System.currentTimeMillis();
        logger.info("Received login request for phone: {}", loginRequest.getPhone());

        LoginSuccessResponse_LROA938 response = authenticationService.loginAndRequestOtp(loginRequest);

        long endTime = System.currentTimeMillis();
        logger.info("Successfully processed login request for phone: {} in {}ms", loginRequest.getPhone(), (endTime - startTime));
        return ResponseEntity.ok(response);
    }
}
```
```java
// src/test/java/com/yourorg/auth/service/AuthenticationServiceImpl_LROA938Test.java