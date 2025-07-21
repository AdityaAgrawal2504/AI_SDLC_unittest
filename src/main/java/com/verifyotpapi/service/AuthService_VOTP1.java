package com.verifyotpapi.service;

import com.verifyotpapi.config.AppProperties_VOTP1;
import com.verifyotpapi.dto.request.VerifyOtpRequest_VOTP1;
import com.verifyotpapi.dto.response.VerifyOtpSuccessResponse_VOTP1;
import com.verifyotpapi.exception.*;
import com.verifyotpapi.logging.StructuredLogger_VOTP1;
import com.verifyotpapi.model.OtpData_VOTP1;
import com.verifyotpapi.repository.OtpDataRepository_VOTP1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

/**
 * Implements the business logic for OTP verification and session creation.
 */
@Service
@RequiredArgsConstructor
public class AuthService_VOTP1 implements IAuthService_VOTP1 {

    private final OtpDataRepository_VOTP1 otpRepository;
    private final ITokenService_VOTP1 tokenService;
    private final PasswordHashingService_VOTP1 passwordHashingService;
    private final AppProperties_VOTP1 appProperties;
    private final StructuredLogger_VOTP1 logger;

    /**
     * Verifies the OTP, handling all validation and error scenarios, and issues JWTs on success.
     */
    @Override
    @Transactional
    public VerifyOtpSuccessResponse_VOTP1 verifyOtp(VerifyOtpRequest_VOTP1 request) {
        long startTime = System.currentTimeMillis();
        logger.logFunctionStart("verifyOtp");

        OtpData_VOTP1 otpData = otpRepository.findByVerificationToken(request.getVerificationToken())
            .orElseThrow(() -> {
                logger.warn("Verification token not found", Map.of("token", request.getVerificationToken()));
                return new ResourceNotFoundException_VOTP1();
            });

        // 1. Check for expiration
        if (otpData.getExpiresAt().isBefore(Instant.now())) {
            otpRepository.delete(otpData);
            logger.warn("Verification token expired", Map.of("token", request.getVerificationToken(), "expiresAt", otpData.getExpiresAt()));
            throw new TokenExpiredException_VOTP1();
        }

        // 2. Check for max attempts
        if (otpData.getAttempts() >= appProperties.otp().maxAttempts()) {
            otpRepository.delete(otpData);
            logger.warn("Max verification attempts reached", Map.of("token", request.getVerificationToken(), "attempts", otpData.getAttempts()));
            throw new MaxAttemptsExceededException_VOTP1();
        }

        // 3. Verify OTP code
        boolean isOtpValid = passwordHashingService.matches(request.getOtpCode(), otpData.getHashedOtp());

        if (!isOtpValid) {
            otpRepository.incrementAttempts(request.getVerificationToken());
            logger.warn("Invalid OTP code provided", Map.of("token", request.getVerificationToken(), "attempts", otpData.getAttempts() + 1));
            throw new InvalidOtpException_VOTP1();
        }

        // 4. Success: Generate tokens and clean up
        VerifyOtpSuccessResponse_VOTP1 response = tokenService.generateSessionTokens(otpData.getUserId());
        otpRepository.delete(otpData); // Invalidate the OTP after successful use

        logger.info("OTP verification successful", Map.of("userId", otpData.getUserId()));
        logger.logFunctionEnd("verifyOtp", startTime);

        return response;
    }
}
```
```java
// src/main/java/com/verifyotpapi/controller/AuthController_VOTP1.java