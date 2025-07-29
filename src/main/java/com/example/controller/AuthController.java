src/main/java/com/example/controller/AuthController.java
package com.example.controller;

import com.example.dto.AuthTokensDto;
import com.example.dto.OtpVerificationDto;
import com.example.dto.UserLoginDto;
import com.example.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Handles '/auth/*' endpoints for login and OTP verification.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;

    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates a user and triggers an OTP send.
     * @param userLoginDto DTO containing phone number and password.
     * @return ResponseEntity with status 202 (Accepted).
     */
    @PostMapping("/otp/request")
    public ResponseEntity<Void> requestLoginOtp(@Valid @RequestBody UserLoginDto userLoginDto) {
        authService.requestLoginOtp(userLoginDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * Verifies the submitted OTP and returns session tokens.
     * @param otpVerificationDto DTO containing phone number and OTP.
     * @return ResponseEntity with AuthTokensDto on success.
     */
    @PostMapping("/otp/verify")
    public ResponseEntity<AuthTokensDto> verifyLoginOtp(@Valid @RequestBody OtpVerificationDto otpVerificationDto) {
        AuthTokensDto tokens = authService.verifyLoginOtp(otpVerificationDto);
        return ResponseEntity.ok(tokens);
    }
}