package com.example.service;

import com.example.dto.request.LoginInitiateRequest;
import com.example.dto.request.LoginVerifyRequest;
import com.example.dto.request.UserSignupRequest;
import com.example.dto.response.LoginInitiateResponse;
import com.example.dto.response.LoginVerifyResponse;
import com.example.dto.response.UserSignupResponse;

/**
 * Service interface for authentication operations.
 */
public interface IAuthService {
    /**
     * Registers a new user.
     * @param request DTO with signup information.
     * @return DTO with created user details.
     */
    UserSignupResponse signUp(UserSignupRequest request);

    /**
     * Initiates a login by verifying credentials and sending an OTP.
     * @param request DTO with login credentials.
     * @return DTO with the login session ID.
     */
    LoginInitiateResponse initiateLogin(LoginInitiateRequest request);

    /**
     * Verifies an OTP to complete login and issue a token.
     * @param request DTO with login session ID and OTP.
     * @return DTO with the JWT access token.
     */
    LoginVerifyResponse verifyLogin(LoginVerifyRequest request);
}