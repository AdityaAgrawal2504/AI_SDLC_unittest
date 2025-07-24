package com.example.service;

import com.example.repository.IOtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements IOtpService {

    private static final int OTP_LENGTH = 6;
    private static final long OTP_EXPIRY_SECONDS = 300; // 5 minutes
    private static final String OTP_KEY_PREFIX = "otp:";

    private final IOtpRepository otpRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    
    /**
     * Generates a 6-digit OTP, stores it, and returns it.
     * In a real application, this would send the OTP via SMS and not return it.
     * @param phoneNumber The user's phone number, used as a key.
     * @return The generated OTP.
     */
    @Override
    public String generateAndStoreOtp(String phoneNumber) {
        String otp = String.format("%0" + OTP_LENGTH + "d", secureRandom.nextInt((int) Math.pow(10, OTP_LENGTH)));
        otpRepository.save(getOtpKey(phoneNumber), otp, OTP_EXPIRY_SECONDS);
        // NOTE: In a production system, you would send the OTP via SMS here and not return it from the method.
        // For this example, we return it to simulate the process.
        return otp;
    }

    /**
     * Validates a given OTP for a phone number.
     * @param phoneNumber The user's phone number.
     * @param otp The OTP provided by the user.
     * @return true if the OTP is valid and not expired, false otherwise.
     */
    @Override
    public boolean validateOtp(String phoneNumber, String otp) {
        String key = getOtpKey(phoneNumber);
        return otpRepository.get(key)
                .map(storedOtp -> {
                    boolean isValid = storedOtp.equals(otp);
                    if (isValid) {
                        otpRepository.delete(key); // OTP is single-use
                    }
                    return isValid;
                })
                .orElse(false);
    }

    private String getOtpKey(String phoneNumber) {
        return OTP_KEY_PREFIX + phoneNumber;
    }
}