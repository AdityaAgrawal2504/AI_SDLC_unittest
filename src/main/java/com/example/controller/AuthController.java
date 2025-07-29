package com.example.controller;

import com.example.dto.*;
import com.example.model.User;
import com.example.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;
    private final long tokenExpirationMs;

    public AuthController(IAuthService authService, @Value("${jwt.expiration.access-token}") long tokenExpirationMs) {
        this.authService = authService;
        this.tokenExpirationMs = tokenExpirationMs;
    }

    /**
     * Handles POST /auth/signup. Registers a new user.
     * @param signupDto The user signup data.
     * @return The created user's information.
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> userSignup(@Valid @RequestBody UserSignupDto signupDto) {
        User newUser = authService.registerUser(signupDto);
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(newUser.getId());
        responseDto.setName(newUser.getName());
        responseDto.setPhoneNumber(newUser.getPhoneNumber());
        responseDto.setCreatedAt(newUser.getCreatedAt());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Handles POST /auth/login/initiate. Validates credentials and sends an OTP.
     * @param loginInitiateDto The user's login credentials.
     * @return A success message.
     */
    @PostMapping("/login/initiate")
    public ResponseEntity<?> initiateLogin(@Valid @RequestBody LoginInitiateDto loginInitiateDto) {
        authService.initiateLogin(loginInitiateDto);
        return ResponseEntity.ok().body(java.util.Map.of("message", "OTP sent to your registered phone number."));
    }

    /**
     * Handles POST /auth/login/verify. Verifies OTP and returns an authentication token.
     * @param loginVerifyDto The phone number and OTP.
     * @return An authentication token response.
     */
    @PostMapping("/login/verify")
    public ResponseEntity<AuthTokenResponseDto> verifyLogin(@Valid @RequestBody LoginVerifyDto loginVerifyDto) {
        String token = authService.completeLogin(loginVerifyDto);
        AuthTokenResponseDto response = new AuthTokenResponseDto(token, "Bearer", tokenExpirationMs / 1000);
        return ResponseEntity.ok(response);
    }
}