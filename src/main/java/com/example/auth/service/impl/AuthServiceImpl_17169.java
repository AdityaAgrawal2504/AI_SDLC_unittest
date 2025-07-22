package com.example.auth.service.impl;

import com.example.auth.dto.request.VerifyOtpRequest_17169;
import com.example.auth.dto.response.VerifyOtpSuccessResponse_17169;
import com.example.auth.entity.OtpRecord_17169;
import com.example.auth.enums.ErrorCode_AuthVerifyOtp_17169;
import com.example.auth.exception.ApplicationException_AuthVerifyOtp_17169;
import com.example.auth.security.JwtTokenProvider_17169;
import com.example.auth.service.AuthService_17169;
import com.example.auth.service.OtpService_17169;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements the core business logic for the OTP verification and session creation flow.
 */
@Service
public class AuthServiceImpl_17169 implements AuthService_17169 {

    private final OtpService_17169 otpService;
    private final JwtTokenProvider_17169 jwtTokenProvider;
    private final long tokenExpirationSeconds;

    public AuthServiceImpl_17169(OtpService_17169 otpService,
                                 JwtTokenProvider_17169 jwtTokenProvider,
                                 @Value("${auth.jwt.expiration-seconds}") long tokenExpirationSeconds) {
        this.otpService = otpService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenExpirationSeconds = tokenExpirationSeconds;
    }

    /**
     * Handles the end-to-end logic for verifying an OTP and creating a new user session.
     * This process is transactional to ensure data consistency.
     */
    @Override
    @Transactional
    public VerifyOtpSuccessResponse_17169 verifyOtpAndCreateSession(VerifyOtpRequest_17169 request) {
        try {
            // 1. Find and validate the OTP. This will throw an exception on failure.
            OtpRecord_17169 otpRecord = otpService.findAndValidateActiveOtp(request.getPhoneNumber(), request.getOtp());

            // 2. Generate a session token
            String token = jwtTokenProvider.createToken(otpRecord.getUserId(), otpRecord.getPhoneNumber());

            // 3. Invalidate the OTP so it cannot be reused
            otpService.invalidateOtp(otpRecord);

            // 4. Build and return the success response
            return VerifyOtpSuccessResponse_17169.builder()
                    .sessionToken(token)
                    .expiresIn(tokenExpirationSeconds)
                    .build();
        } catch (ApplicationException_AuthVerifyOtp_17169 e) {
            // Re-throw known application exceptions
            throw e;
        } catch (Exception e) {
            // Catch any other unexpected exception during token generation and wrap it
            throw new ApplicationException_AuthVerifyOtp_17169(ErrorCode_AuthVerifyOtp_17169.SESSION_CREATION_FAILED);
        }
    }
}
src/main/java/com/example/auth/exception/GlobalExceptionHandler_AuthVerifyOtp_17169.java