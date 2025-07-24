package com.example.messagingapp.controller;

import com.example.messagingapp.dto.AuthTokenResponse;
import com.example.messagingapp.dto.LoginInitiateRequest;
import com.example.messagingapp.dto.LoginVerifyRequest;
import com.example.messagingapp.logging.Loggable;
import com.example.messagingapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Handles the request to initiate a login by sending an OTP.
     * @param request The login initiation request DTO.
     * @return A response entity confirming OTP has been sent.
     */
    @PostMapping("/login/initiate")
    @Loggable
    public ResponseEntity<Map<String, String>> initiateLogin(@Valid @RequestBody LoginInitiateRequest request) {
        authService.initiateLogin(request);
        return ResponseEntity.ok(Map.of("message", "OTP has been sent to your phone number."));
    }

    /**
     * Handles the request to verify an OTP and complete the login.
     * @param request The login verification request DTO.
     * @return A response entity containing the authentication token.
     */
    @PostMapping("/login/verify")
    @Loggable
    public ResponseEntity<AuthTokenResponse> verifyLogin(@Valid @RequestBody LoginVerifyRequest request) {
        AuthTokenResponse response = authService.verifyLogin(request);
        return ResponseEntity.ok(response);
    }
}