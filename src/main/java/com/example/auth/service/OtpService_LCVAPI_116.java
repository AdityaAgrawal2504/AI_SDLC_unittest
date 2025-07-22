package com.example.auth.service;

import com.example.auth.exception.CustomApiException_LCVAPI_114;
import com.example.auth.enums.ErrorCode_LCVAPI_107;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A mock service to simulate sending a One-Time Password (OTP).
 */
@Service
@Log4j2
public class OtpService_LCVAPI_116 {

    /**
     * Simulates generating and sending an OTP to a given phone number.
     * This mock implementation includes a random failure to test error handling.
     * @param phoneNumber The phone number to send the OTP to.
     */
    public void sendOtp(String phoneNumber) {
        long startTime = System.currentTimeMillis();
        // Simulate a 10% chance of failure for the external OTP service
        if (ThreadLocalRandom.current().nextInt(10) == 0) {
            log.error("OTP Service Failure: Could not dispatch OTP to {}. Took {}ms", phoneNumber, System.currentTimeMillis() - startTime);
            throw new CustomApiException_LCVAPI_114(ErrorCode_LCVAPI_107.OTP_SERVICE_UNAVAILABLE);
        }

        String otp = String.format("%06d", new SecureRandom().nextInt(999999));
        log.info("Mock OTP Dispatch: Successfully sent OTP {} to {}. Took {}ms", otp, phoneNumber, System.currentTimeMillis() - startTime);
        // In a real application, an external client (e.g., Twilio) would be called here.
    }
}
src/main/java/com/example/auth/util/RequestIdUtil_LCVAPI_111.java