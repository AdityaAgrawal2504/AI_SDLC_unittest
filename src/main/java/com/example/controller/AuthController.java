package com.example.controller;

import com.example.dto.RequestOtpRequest;
import com.example.dto.VerifyOtpRequest;
import com.example.dto.VerifyOtpResponse;
import com.example.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    /**
     * Handles POST /auth/login/request-otp
     * @param request The request containing credentials.
     * @return A success message.
     */
    @Operation(summary = "Accepts a phone number and password, validates them, and sends an OTP.")
    @PostMapping("/login/request-otp")
    public ResponseEntity<Map<String, String>> requestLoginOtp(@Valid @RequestBody RequestOtpRequest request) {
        authService.requestLoginOtp(request);
        return ResponseEntity.ok(Map.of("message", "OTP has been sent to your phone number."));
    }

    /**
     * Handles POST /auth/login/verify-otp
     * @param request The request containing the OTP.
     * @return The authentication token.
     */
    @Operation(summary = "Verifies the OTP and returns an authentication token.")
    @PostMapping("/login/verify-otp")
    public ResponseEntity<VerifyOtpResponse> verifyLoginOtp(@Valid @RequestBody VerifyOtpRequest request) {
        VerifyOtpResponse response = authService.verifyLoginOtp(request);
        return ResponseEntity.ok(response);
    }
}