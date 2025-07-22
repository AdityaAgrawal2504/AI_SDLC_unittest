package com.example.auth.service;

import com.example.auth.dto.request.VerifyOtpRequest_17169;
import com.example.auth.dto.response.VerifyOtpSuccessResponse_17169;

/**
 * Interface defining the contract for the primary authentication service logic.
 */
public interface AuthService_17169 {

    /**
     * Verifies an OTP and creates a user session upon successful validation.
     * @param request The request DTO containing the phone number and OTP.
     * @return A response DTO containing the session JWT.
     */
    VerifyOtpSuccessResponse_17169 verifyOtpAndCreateSession(VerifyOtpRequest_17169 request);
}
src/main/java/com/example/auth/service/impl/AuthServiceImpl_17169.java