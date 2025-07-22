package com.example.auth.service;

import com.example.auth.entity.OtpRecord_17169;

/**
 * Interface defining the contract for OTP management operations.
 */
public interface OtpService_17169 {

    /**
     * Retrieves and validates the active OTP for a given phone number and code.
     * @param phoneNumber The user's phone number.
     * @param otpCode The OTP code to verify.
     * @return The validated OtpRecord.
     */
    OtpRecord_17169 findAndValidateActiveOtp(String phoneNumber, String otpCode);

    /**
     * Invalidates an OTP record by marking it as used.
     * @param otpRecord The OTP record to invalidate.
     */
    void invalidateOtp(OtpRecord_17169 otpRecord);
}
src/main/java/com/example/auth/service/impl/OtpServiceImpl_17169.java