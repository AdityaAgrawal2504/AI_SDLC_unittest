package com.example.service.impl;

import com.example.service.IOtpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.DecimalFormat;

@Service
public class OtpService implements IOtpService {

    private static final Logger log = LogManager.getLogger(OtpService.class);
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a 6-digit numeric OTP.
     * @return The generated OTP as a string.
     */
    @Override
    public String generateOtp() {
        int otp = 100000 + this.secureRandom.nextInt(900000);
        return new DecimalFormat("000000").format(otp);
    }

    /**
     * Sends an OTP to a phone number. In a real application, this would integrate with an SMS gateway like Twilio.
     * @param phoneNumber The recipient's phone number.
     * @param otp The one-time password to send.
     */
    @Override
    public void sendOtp(String phoneNumber, String otp) {
        // This is a mock implementation. In a real-world scenario, you would
        // integrate with an SMS service provider (e.g., Twilio, Vonage).
        log.info("---- OTP Service ----");
        log.info("Sending OTP {} to phone number {}", otp, phoneNumber);
        log.info("---- End OTP Service ----");
    }
}