package com.example.service;

import com.example.model.User;
import java.util.UUID;

/**
 * Service interface for One-Time Password (OTP) generation and verification.
 */
public interface IOtpService {
    /**
     * Generates an OTP for a user, sends it, and returns the login attempt ID.
     * @param user The user for whom to generate the OTP.
     * @return The ID of the created LoginAttempt.
     */
    UUID generateAndSendOtp(User user);

    /**
     * Verifies a provided OTP against a stored login attempt.
     * @param loginAttemptId The ID of the login attempt.
     * @param otp The OTP provided by the user.
     * @return The User associated with the successful verification.
     */
    User verifyOtp(UUID loginAttemptId, String otp);
}