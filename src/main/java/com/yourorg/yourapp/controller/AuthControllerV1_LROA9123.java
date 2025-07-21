package com.yourorg.yourapp.controller;

import com.yourorg.yourapp.dto.request.LoginRequestLROA9123;
import com.yourorg.yourapp.dto.response.LoginSuccessResponseLROA9123;
import com.yourorg.yourapp.service.AuthServiceLROA9123;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling authentication endpoints.
 */
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthControllerV1_LROA9123 {

    private final AuthServiceLROA9123 authService;

    /**
     * Handles the user login request, validates credentials, and sends an OTP.
     * @param loginRequest The request body containing the user's phone and password.
     * @return A response entity with a success message and an OTP session token.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginSuccessResponseLROA9123> loginAndRequestOtp(
            @Valid @RequestBody LoginRequestLROA9123 loginRequest) {
        LoginSuccessResponseLROA9123 response = authService.loginAndRequestOtp(loginRequest);
        return ResponseEntity.ok(response);
    }
}
```
src/main/java/com/yourorg/yourapp/config/DataInitializerLROA9123.java