package com.example.controller;

import com.example.dto.AuthTokenResponse_UATH_1002;
import com.example.dto.GenericSuccessResponse_UATH_1003;
import com.example.dto.LoginInitiateRequest_UATH_1004;
import com.example.dto.OtpVerifyRequest_UATH_1005;
import com.example.dto.UserRegistrationRequest_UATH_1006;
import com.example.dto.UserRegistrationResponse_UATH_1007;
import com.example.service.AuthService_UATH_1008;
import com.example.service.UserService_UATH_1009;
import com.example.util.StructuredLogger_UTIL_9999;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user registration and authentication endpoints.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserAuthController_UATH_1001 {

    private final UserService_UATH_1009 userService;
    private final AuthService_UATH_1008 authService;
    private final StructuredLogger_UTIL_9999 logger;

    /**
     * Registers a new user account.
     */
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse_UATH_1007> registerUser(@Valid @RequestBody UserRegistrationRequest_UATH_1006 request) {
        return logger.logMethod("registerUser", () -> {
            logger.info("Registration attempt for phone: " + request.phoneNumber());
            UserRegistrationResponse_UATH_1007 response = userService.registerUser(request);
            logger.info("Registration successful for user ID: " + response.userId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        });
    }

    /**
     * Initiates the login process by sending an OTP.
     */
    @PostMapping("/auth/initiate")
    public ResponseEntity<GenericSuccessResponse_UATH_1003> initiateLogin(@Valid @RequestBody LoginInitiateRequest_UATH_1004 request) {
        return logger.logMethod("initiateLogin", () -> {
            logger.info("Login initiation for phone: " + request.phoneNumber());
            GenericSuccessResponse_UATH_1003 response = authService.initiateLogin(request);
            logger.info("OTP sent for phone: " + request.phoneNumber());
            return ResponseEntity.ok(response);
        });
    }

    /**
     * Verifies the OTP and provides authentication tokens upon success.
     */
    @PostMapping("/auth/verify")
    public ResponseEntity<AuthTokenResponse_UATH_1002> verifyLogin(@Valid @RequestBody OtpVerifyRequest_UATH_1005 request) {
        return logger.logMethod("verifyLogin", () -> {
            logger.info("OTP verification for phone: " + request.phoneNumber());
            AuthTokenResponse_UATH_1002 response = authService.verifyOtpAndLogin(request);
            logger.info("Login successful for phone: " + request.phoneNumber());
            return ResponseEntity.ok(response);
        });
    }
}
```
```java
//
// Filename: src/main/java/com/example/controller/ConversationController_CHAT_2001.java
//