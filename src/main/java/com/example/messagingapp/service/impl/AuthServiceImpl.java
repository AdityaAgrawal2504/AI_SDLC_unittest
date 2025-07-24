package com.example.messagingapp.service.impl;

import com.example.messagingapp.dto.AuthTokenResponse;
import com.example.messagingapp.dto.LoginInitiateRequest;
import com.example.messagingapp.dto.LoginVerifyRequest;
import com.example.messagingapp.exception.ApiException;
import com.example.messagingapp.model.User;
import com.example.messagingapp.service.AuthService;
import com.example.messagingapp.service.OTPService;
import com.example.messagingapp.service.TokenService;
import com.example.messagingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OTPService otpService;
    private final TokenService tokenService;

    /**
     * Validates user credentials and initiates OTP sending.
     * @param request DTO containing phone number and password.
     */
    @Override
    public void initiateLogin(LoginInitiateRequest request) {
        User user = userService.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "INVALID_CREDENTIALS", "Invalid phone number or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "INVALID_CREDENTIALS", "Invalid phone number or password.");
        }

        otpService.generateAndSend(request.getPhoneNumber());
    }

    /**
     * Verifies the OTP and returns an authentication token upon success.
     * @param request DTO containing phone number and OTP.
     * @return DTO containing the access token.
     */
    @Override
    public AuthTokenResponse verifyLogin(LoginVerifyRequest request) {
        if (!otpService.verify(request.getPhoneNumber(), request.getOtp())) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "OTP_INVALID_OR_EXPIRED", "The provided OTP is invalid or has expired.");
        }

        User user = userService.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found after OTP verification."));

        String accessToken = tokenService.generateAccessToken(user);
        return new AuthTokenResponse(accessToken);
    }
}