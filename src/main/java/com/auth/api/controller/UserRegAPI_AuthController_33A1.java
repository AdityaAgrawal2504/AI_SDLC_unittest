package com.auth.api.controller;

import com.auth.api.dto.UserRegAPI_UserRegistrationRequest_33A1;
import com.auth.api.dto.UserRegAPI_UserRegistrationResponse_33A1;
import com.auth.api.logging.UserRegAPI_Loggable_33A1;
import com.auth.api.service.UserRegAPI_AuthService_33A1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling user authentication endpoints like registration.
 */
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class UserRegAPI_AuthController_33A1 {

    private final UserRegAPI_AuthService_33A1 authService;

    /**
     * Handles the POST request to register a new user.
     * @param request The request body containing phone number and password.
     * @return A ResponseEntity with the registration result and 201 Created status.
     */
    @PostMapping("/register")
    @UserRegAPI_Loggable_33A1
    public ResponseEntity<UserRegAPI_UserRegistrationResponse_33A1> registerUser(
            @Valid @RequestBody UserRegAPI_UserRegistrationRequest_33A1 request) {
        UserRegAPI_UserRegistrationResponse_33A1 response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
```
```java
// src/main/java/com/auth/api/exception/UserRegAPI_GlobalExceptionHandler_33A1.java