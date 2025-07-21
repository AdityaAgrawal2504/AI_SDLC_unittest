package com.yourorg.verifyotp.service;

import com.yourorg.verifyotp.constants.ErrorCodeVAPI_1;
import com.yourorg.verifyotp.dto.VerifyOtpRequestVAPI_1;
import com.yourorg.verifyotp.dto.VerifyOtpSuccessResponseVAPI_1;
import com.yourorg.verifyotp.exception.CustomApiExceptionVAPI_1;
import com.yourorg.verifyotp.model.OtpDataVAPI_1;
import com.yourorg.verifyotp.repository.OtpRepositoryVAPI_1;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuthServiceVAPI_1 implements IAuthServiceVAPI_1 {

    private final OtpRepositoryVAPI_1 otpRepository;
    private final ITokenServiceVAPI_1 tokenService;
    private final PasswordEncoder passwordEncoder;
    private final int maxAttempts;

    public AuthServiceVAPI_1(OtpRepositoryVAPI_1 otpRepository,
                             ITokenServiceVAPI_1 tokenService,
                             @Qualifier("passwordEncoderVAPI_1") PasswordEncoder passwordEncoder,
                             @Value("${otp.verification.max.attempts:3}") int maxAttempts) {
        this.otpRepository = otpRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.maxAttempts = maxAttempts;
    }

    /**
     * Verifies the user-submitted OTP against the stored hash.
     * On success, generates session tokens and cleans up the OTP record.
     * On failure, increments the attempt counter.
     */
    @Override
    @Transactional
    public VerifyOtpSuccessResponseVAPI_1 verifyOtp(VerifyOtpRequestVAPI_1 request) {
        OtpDataVAPI_1 otpData = otpRepository.findByVerificationToken(request.getVerificationToken())
                .orElseThrow(() -> new CustomApiExceptionVAPI_1(ErrorCodeVAPI_1.TOKEN_NOT_FOUND));

        validateOtpState(otpData);

        if (!passwordEncoder.matches(request.getOtpCode(), otpData.getHashedOtp())) {
            handleInvalidOtpAttempt(otpData);
            // This line is effectively unreachable as handleInvalidOtpAttempt always throws.
            throw new CustomApiExceptionVAPI_1(ErrorCodeVAPI_1.INVALID_OTP);
        }

        // Success case
        otpRepository.delete(otpData);
        return tokenService.generateSessionTokens(otpData.getUserId());
    }

    /**
     * Checks if the OTP has expired or exceeded max attempts.
     */
    private void validateOtpState(OtpDataVAPI_1 otpData) {
        if (otpData.getAttempts() >= maxAttempts) {
            throw new CustomApiExceptionVAPI_1(ErrorCodeVAPI_1.MAX_ATTEMPTS_REACHED);
        }

        if (otpData.getExpiresAt().isBefore(Instant.now())) {
            throw new CustomApiExceptionVAPI_1(ErrorCodeVAPI_1.TOKEN_EXPIRED);
        }
    }

    /**
     * Handles the logic for an incorrect OTP submission by incrementing the attempt count.
     */
    private void handleInvalidOtpAttempt(OtpDataVAPI_1 otpData) {
        otpData.setAttempts(otpData.getAttempts() + 1);
        otpRepository.save(otpData);
        throw new CustomApiExceptionVAPI_1(ErrorCodeVAPI_1.INVALID_OTP);
    }
}
```
```java
// Controller: AuthControllerVAPI_1.java