package com.example.service;

import com.example.exception.OtpServiceException_UATH_1015;
import com.example.exception.OtpSessionNotFoundException_UATH_1013;
import com.example.exception.OtpValidationException_UATH_1012;
import com.example.util.StructuredLogger_UTIL_9999;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service to manage the lifecycle of One-Time Passwords (OTP).
 */
@Service
public class OtpService_UATH_1018 {

    private final int otpLength;
    private final long otpValiditySeconds;
    private final StructuredLogger_UTIL_9999 logger;

    // In a production environment, this should be a distributed cache like Redis.
    private final Map<String, OtpData_UATH_1019> otpCache = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public OtpService_UATH_1018(@Value("${otp.length}") int otpLength,
                               @Value("${otp.validity-duration-seconds}") long otpValiditySeconds,
                               StructuredLogger_UTIL_9999 logger) {
        this.otpLength = otpLength;
        this.otpValiditySeconds = otpValiditySeconds;
        this.logger = logger;
    }

    /**
     * Generates and "sends" an OTP for a given phone number.
     */
    public void generateAndSendOtp(String phoneNumber) {
        String otp = generateOtp();
        otpCache.put(phoneNumber, new OtpData_UATH_1019(otp, Instant.now()));

        // Mock sending the OTP. In a real app, this would integrate with an SMS gateway.
        try {
            // Simulate network call
            Thread.sleep(50); 
            logger.info(String.format("Mock OTP Sent to %s: %s", phoneNumber, otp));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OtpServiceException_UATH_1015("Failed to send OTP due to an interruption.");
        }
    }

    /**
     * Validates the provided OTP for a given phone number.
     */
    public boolean validateOtp(String phoneNumber, String providedOtp) {
        OtpData_UATH_1019 otpData = otpCache.get(phoneNumber);

        if (otpData == null) {
            throw new OtpSessionNotFoundException_UATH_1013("No active OTP verification process found for the given phone number.");
        }

        if (Duration.between(otpData.getTimestamp(), Instant.now()).getSeconds() > otpValiditySeconds) {
            otpCache.remove(phoneNumber); // Clean up expired OTP
            throw new OtpValidationException_UATH_1012("The OTP has expired.");
        }
        
        int attempts = otpData.incrementAttempts();
        if(attempts > 3){
             otpCache.remove(phoneNumber);
             throw new OtpValidationException_UATH_1012("The OTP has been attempted too many times.");
        }

        if (!otpData.getOtp().equals(providedOtp)) {
            throw new OtpValidationException_UATH_1012("The OTP is incorrect.");
        }

        return true;
    }

    /**
     * Clears the OTP from the cache after successful validation.
     */
    public void clearOtp(String phoneNumber) {
        otpCache.remove(phoneNumber);
    }

    /**
     * Generates a random numeric string of the configured length.
     */
    private String generateOtp() {
        StringBuilder otp = new StringBuilder(otpLength);
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
```
```java
//
// Filename: src/main/java/com/example/service/OtpData_UATH_1019.java
//