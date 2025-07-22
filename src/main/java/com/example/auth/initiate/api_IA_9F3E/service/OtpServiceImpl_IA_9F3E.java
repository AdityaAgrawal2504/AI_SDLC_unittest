package com.example.auth.initiate.api_IA_9F3E.service;

import com.example.auth.initiate.api_IA_9F3E.exception.OtpServiceException_IA_9F3E;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * Mock implementation of the OTP service.
 */
@Service
public class OtpServiceImpl_IA_9F3E implements IOtpService_IA_9F3E {

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl_IA_9F3E.class);
    private final SecureRandom random = new SecureRandom();

    /**
     * Simulates generating and sending an OTP via an external provider.
     * @param phoneNumber The recipient's phone number.
     * @param transactionId The unique ID correlating the login attempt.
     */
    @Override
    public void sendLoginOtp(String phoneNumber, String transactionId) {
        // Simulate a 5% failure rate for the OTP service
        if (Math.random() < 0.05) {
            logger.error("Simulating OTP service failure for transactionId: {}", transactionId);
            throw new OtpServiceException_IA_9F3E();
        }

        // In a real application, you would generate a real OTP
        String otp = String.format("%06d", random.nextInt(999999));
        
        // In a real application, you would store the OTP hash with the transactionId in a cache (e.g., Redis)
        // with a short TTL (e.g., 5 minutes).
        // e.g., redisTemplate.opsForValue().set("otp:" + transactionId, hash(otp), 5, TimeUnit.MINUTES);
        
        // Simulate sending the OTP via an SMS gateway
        logger.info("--- MOCK OTP SERVICE ---");
        logger.info("Sending OTP {} to phone number {} for transactionId: {}", otp, phoneNumber, transactionId);
        logger.info("--- END MOCK OTP SERVICE ---");
    }
}
```
```java