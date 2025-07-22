package com.example.auth.controller;

import com.example.auth.dto.request.VerifyOtpRequest_17169;
import com.example.auth.dto.response.VerifyOtpSuccessResponse_17169;
import com.example.auth.service.AuthService_17169;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling authentication-related API endpoints.
 */
@RestController
@RequestMapping("/auth")
public class AuthController_17169 {

    private final AuthService_17169 authService;

    public AuthController_17169(AuthService_17169 authService) {
        this.authService = authService;
    }

    /**
     * Handles the POST request to verify an OTP and create a user session.
     * @param request The request body containing phone number and OTP.
     * @return A response entity with the session token on success, or an error response.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOtpSuccessResponse_17169> verifyOtp(@Valid @RequestBody VerifyOtpRequest_17169 request) {
        VerifyOtpSuccessResponse_17169 response = authService.verifyOtpAndCreateSession(request);
        return ResponseEntity.ok(response);
    }
}
src/main/java/com/example/auth/config/WebSecurityConfig_AuthVerifyOtp_17169.java