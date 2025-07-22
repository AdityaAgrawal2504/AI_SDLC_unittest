package com.example.auth.service.impl;

import com.example.auth.entity.OtpRecord_17169;
import com.example.auth.enums.ErrorCode_AuthVerifyOtp_17169;
import com.example.auth.exception.ApplicationException_AuthVerifyOtp_17169;
import com.example.auth.repository.OtpRepository_17169;
import com.example.auth.service.OtpService_17169;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

/**
 * Implements the business logic for managing and verifying One-Time Passwords.
 */
@Service
public class OtpServiceImpl_17169 implements OtpService_17169 {

    private final OtpRepository_17169 otpRepository;
    private final int maxAttempts;

    public OtpServiceImpl_17169(OtpRepository_17169 otpRepository, @Value("${auth.otp.max-attempts}") int maxAttempts) {
        this.otpRepository = otpRepository;
        this.maxAttempts = maxAttempts;
    }

    /**
     * Retrieves, validates, and returns the active OTP record.
     * This method is transactional to ensure atomic updates for attempt counts.
     */
    @Override
    @Transactional
    public OtpRecord_17169 findAndValidateActiveOtp(String phoneNumber, String otpCode) {
        OtpRecord_17169 otpRecord = otpRepository.findFirstByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(phoneNumber)
                .orElseThrow(() -> new ApplicationException_AuthVerifyOtp_17169(ErrorCode_AuthVerifyOtp_17169.OTP_NOT_FOUND));

        if (otpRecord.getAttempts() >= maxAttempts) {
            throw new ApplicationException_AuthVerifyOtp_17169(ErrorCode_AuthVerifyOtp_17169.TOO_MANY_ATTEMPTS);
        }

        if (ZonedDateTime.now().isAfter(otpRecord.getExpiresAt())) {
            invalidateOtp(otpRecord); // Expired OTPs should be marked as used
            throw new ApplicationException_AuthVerifyOtp_17169(ErrorCode_AuthVerifyOtp_17169.OTP_EXPIRED);
        }

        if (!otpRecord.getOtpCode().equals(otpCode)) {
            otpRecord.setAttempts(otpRecord.getAttempts() + 1);
            otpRepository.save(otpRecord);
            throw new ApplicationException_AuthVerifyOtp_17169(ErrorCode_AuthVerifyOtp_17169.OTP_INCORRECT);
        }

        return otpRecord;
    }

    /**
     * Marks a given OTP record as used and persists the change.
     */
    @Override
    @Transactional
    public void invalidateOtp(OtpRecord_17169 otpRecord) {
        otpRecord.setUsed(true);
        otpRepository.save(otpRecord);
    }
}
src/main/java/com/example/auth/service/AuthService_17169.java