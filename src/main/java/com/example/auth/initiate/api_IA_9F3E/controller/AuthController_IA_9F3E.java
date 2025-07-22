package com.example.auth.initiate.api_IA_9F3E.controller;

import com.example.auth.initiate.api_IA_9F3E.dto.InitiateLoginRequestDTO_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.dto.InitiateLoginResponseDTO_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.service.IAuthService_IA_9F3E;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling authentication-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController_IA_9F3E {

    private final IAuthService_IA_9F3E authService;

    public AuthController_IA_9F3E(IAuthService_IA_9F3E authService) {
        this.authService = authService;
    }

    /**
     * Handles the POST request to initiate the login process.
     * @param request The request body containing user credentials.
     * @return A response entity with the result of the login initiation.
     */
    @PostMapping("/initiate")
    public ResponseEntity<InitiateLoginResponseDTO_IA_9F3E> initiateLogin(
            @Valid @RequestBody InitiateLoginRequestDTO_IA_9F3E request) {
        InitiateLoginResponseDTO_IA_9F3E response = authService.initiateLogin(request);
        return ResponseEntity.ok(response);
    }
}
```
```java