package com.example.auth.service;

import com.example.auth.dto.LoginRequest_LCVAPI_104;
import com.example.auth.dto.LoginSuccessResponse_LCVAPI_105;

/**
 * Interface defining the contract for authentication-related business logic.
 */
public interface IAuthenticationService_LCVAPI_117 {

    /**
     * Handles the business logic for user credential validation and triggers the OTP service.
     * @param request The login request DTO containing credentials.
     * @return A response DTO indicating successful OTP dispatch.
     */
    LoginSuccessResponse_LCVAPI_105 loginWithCredentials(LoginRequest_LCVAPI_104 request);
}
src/main/java/com/example/auth/service/OtpService_LCVAPI_116.java