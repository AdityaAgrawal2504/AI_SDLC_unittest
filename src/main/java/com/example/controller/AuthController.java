package com.example.controller;

import com.example.dto.request.LoginInitiateRequest;
import com.example.dto.request.LoginVerifyRequest;
import com.example.dto.request.UserSignupRequest;
import com.example.dto.response.LoginInitiateResponse;
import com.example.dto.response.LoginVerifyResponse;
import com.example.dto.response.UserSignupResponse;
import com.example.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling user authentication, including signup and login.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    /**
     * Registers a new user account.
     * @param request The user signup details.
     * @return A response entity containing the created user's information.
     */
    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponse> createUserAccount(@Valid @RequestBody UserSignupRequest request) {
        UserSignupResponse response = authService.signUp(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Initiates the login process by sending an OTP.
     * @param request The user's login credentials (phone number and password).
     * @return A response entity containing the login session ID.
     */
    @PostMapping("/login/initiate")
    public ResponseEntity<LoginInitiateResponse> initiateLoginSession(@Valid @RequestBody LoginInitiateRequest request) {
        LoginInitiateResponse response = authService.initiateLogin(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Verifies the OTP to complete the login process and returns a JWT.
     * @param request The login session ID and the OTP.
     * @return A response entity containing the JWT access token.
     */
    @PostMapping("/login/verify")
    public ResponseEntity<LoginVerifyResponse> verifyLoginOtp(@Valid @RequestBody LoginVerifyRequest request) {
        LoginVerifyResponse response = authService.verifyLogin(request);
        return ResponseEntity.ok(response);
    }
}