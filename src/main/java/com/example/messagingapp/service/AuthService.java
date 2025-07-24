package com.example.messagingapp.service;

import com.example.messagingapp.dto.AuthTokenResponse;
import com.example.messagingapp.dto.LoginInitiateRequest;
import com.example.messagingapp.dto.LoginVerifyRequest;

public interface AuthService {
    /**
     * Validates user credentials and initiates OTP sending.
     * @param request DTO containing phone number and password.
     */
    void initiateLogin(LoginInitiateRequest request);

    /**
     * Verifies the OTP and returns an authentication token upon success.
     * @param request DTO containing phone number and OTP.
     * @return DTO containing the access token.
     */
    AuthTokenResponse verifyLogin(LoginVerifyRequest request);
}