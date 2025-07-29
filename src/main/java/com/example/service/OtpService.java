package com.example.service;

import com.example.model.LoginAttempt;
import com.example.model.User;
import com.example.repository.ILoginAttemptRepository;
import com.example.service.exception.BadRequestException;
import com.example.service.exception.UnauthorizedException;
import com.example.service.provider.ISmsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

/**
 * Implements OTP generation, sending, and verification logic.
 */
@Service
@RequiredArgsConstructor
public class OtpService implements IOtpService {

    private final ILoginAttemptRepository loginAttemptRepository;
    private final IPasswordService passwordService;
    private final ISmsProvider smsProvider;
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a 6-digit OTP, hashes it, saves it, and sends it via SMS.
     * @param user The user for whom to generate the OTP.
     * @return The ID of the created LoginAttempt.
     */
    @Override
    @Transactional
    public UUID generateAndSendOtp(User user) {
        String otp = String.format("%06d", secureRandom.nextInt(999999));
        
        LoginAttempt attempt = LoginAttempt.builder()
                .user(user)
                .otpHash(passwordService.hashPassword(otp))
                .expiresAt(Instant.now().plusSeconds(300)) // 5 minute expiry
                .isVerified(false)
                .build();
        
        LoginAttempt savedAttempt = loginAttemptRepository.save(attempt);
        
        smsProvider.sendSms(user.getPhoneNumber(), "Your verification code is: " + otp);
        
        return savedAttempt.getId();
    }

    /**
     * Verifies a user-provided OTP against the stored hash for a login attempt.
     * @param loginAttemptId The ID of the login attempt.
     * @param otp The OTP provided by the user.
     * @return The User associated with the successful verification.
     */
    @Override
    @Transactional
    public User verifyOtp(UUID loginAttemptId, String otp) {
        LoginAttempt attempt = loginAttemptRepository.findById(loginAttemptId)
                .orElseThrow(() -> new UnauthorizedException("Invalid login session."));

        if (attempt.isVerified()) {
            throw new BadRequestException("This OTP has already been used.");
        }
        if (attempt.getExpiresAt().isBefore(Instant.now())) {
            throw new UnauthorizedException("OTP has expired.");
        }
        if (!passwordService.comparePassword(otp, attempt.getOtpHash())) {
            throw new UnauthorizedException("Invalid OTP.");
        }

        attempt.setVerified(true);
        loginAttemptRepository.save(attempt);

        return attempt.getUser();
    }
}