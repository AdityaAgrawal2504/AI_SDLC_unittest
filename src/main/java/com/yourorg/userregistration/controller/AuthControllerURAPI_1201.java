package com.yourorg.userregistration.controller;

import com.yourorg.userregistration.dto.UserRegistrationRequestURAPI_1201;
import com.yourorg.userregistration.dto.UserRegistrationResponseURAPI_1201;
import com.yourorg.userregistration.service.IAuthServiceURAPI_1201;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling user authentication and registration endpoints.
 */
@RestController
@RequestMapping("/v1/auth")
public class AuthControllerURAPI_1201 {

    private final IAuthServiceURAPI_1201 authService;

    public AuthControllerURAPI_1201(IAuthServiceURAPI_1201 authService) {
        this.authService = authService;
    }

    /**
     * Registers a new user account.
     * @param request The request body containing phone number and password.
     * @return A response entity with the new user's ID on success.
     */
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseURAPI_1201> registerUser(@Valid @RequestBody UserRegistrationRequestURAPI_1201 request) {
        UserRegistrationResponseURAPI_1201 response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
```
```java
// src/main/java/com/yourorg/userregistration/exception/GlobalExceptionHandlerURAPI_1201.java