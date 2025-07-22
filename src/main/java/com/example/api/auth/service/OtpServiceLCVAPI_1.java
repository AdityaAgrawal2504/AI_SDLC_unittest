package com.example.api.auth.service;

import com.example.api.auth.exception.OtpServiceFailureExceptionLCVAPI_1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Mock implementation of an OTP service.
 * In a real application, this would integrate with an external SMS provider like Twilio.
 */
@Service
public class OtpServiceLCVAPI_1 {

    private static final Logger logger = LogManager.getLogger(OtpServiceLCVAPI_1.class);
    private final Random random = new SecureRandom();

    /**
     * Generates and "sends" an OTP to the given phone number.
     * This mock implementation logs the OTP instead of sending it.
     * It can also be configured to simulate failures.
     * @param phoneNumber The target phone number.
     */
    public void sendOtp(String phoneNumber) {
        long startTime = System.currentTimeMillis();
        // Simulate a potential failure in the OTP service (e.g., 10% chance of failure).
        if (random.nextInt(10) == 0) {
            logger.error("Simulated failure communicating with external OTP service for phone number: {}", phoneNumber);
            throw new OtpServiceFailureExceptionLCVAPI_1("Failed to communicate with external OTP provider.");
        }

        String otp = String.format("%06d", random.nextInt(999999));

        // In a real application, an HTTP client call to an SMS gateway would be made here.
        // For this example, we just log the action.
        logger.info(">>> MOCK OTP SERVICE <<< Sending OTP '{}' to phone number {}.", otp, phoneNumber);
        long endTime = System.currentTimeMillis();
        logger.info("sendOtp execution time: {} ms", (endTime - startTime));
    }
}
```

src/main/java/com/example/api/auth/exception/ErrorCodeLCVAPI_1.java
```java