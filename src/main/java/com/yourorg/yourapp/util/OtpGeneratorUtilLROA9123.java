package com.yourorg.yourapp.util;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;

/**
 * Utility for generating secure one-time passwords (OTPs).
 */
@Component
public class OtpGeneratorUtilLROA9123 {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    /**
     * Generates a 6-digit numeric OTP.
     * @return A string representing the 6-digit OTP.
     */
    public String generateOtp() {
        int number = secureRandom.nextInt((int) Math.pow(10, OTP_LENGTH));
        return String.format("%0" + OTP_LENGTH + "d", number);
    }
}
```
src/main/java/com/yourorg/yourapp/service/OtpServiceClientLROA9123.java
<ctrl60>package com.yourorg.yourapp.service;

import com.yourorg.yourapp.enums.ErrorCodeLROA9123;
import com.yourorg.yourapp.exception.ApiExceptionLROA9123;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Mock implementation of a client to send OTPs via an external service (e.g., SMS Gateway).
 */
@Log4j2
@Service
public class OtpServiceClientLROA9123 {

    /**
     * Simulates sending an OTP to a given phone number.
     * In a real application, this would integrate with a service like Twilio or Vonage.
     * @param phoneNumber The destination phone number.
     * @param otp The one-time password to send.
     */
    public void sendOtp(String phoneNumber, String otp) {
        // Simulate a potential failure of the OTP service.
        if (phoneNumber.endsWith("0000")) {
            log.error("OTP Service Failure: Simulated failure for phone number {}", phoneNumber);
            throw new ApiExceptionLROA9123(ErrorCodeLROA9123.OTP_SERVICE_FAILURE);
        }
        
        // In a real implementation, you would have HTTP client logic here.
        log.info("---- MOCK OTP SERVICE ----");
        log.info("Sending OTP '{}' to phone number '{}'", otp, phoneNumber);
        log.info("--------------------------");
        // Simulate a network delay
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("OTP service client sleep interrupted.");
        }
    }
}
```
src/main/java/com/yourorg/yourapp/aspect/LoggingAspectLROA9123.java
```java