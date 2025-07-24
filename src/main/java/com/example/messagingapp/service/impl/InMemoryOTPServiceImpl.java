package com.example.messagingapp.service.impl;

import com.example.messagingapp.service.OTPService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class InMemoryOTPServiceImpl implements OTPService {

    private static final int OTP_EXPIRATION_MINUTES = 5;
    private final Cache<String, String> otpCache;
    private final SecureRandom random = new SecureRandom();

    public InMemoryOTPServiceImpl() {
        this.otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES)
                .build();
    }

    /**
     * Generates a 6-digit OTP, stores it in an in-memory cache, and logs it.
     * In a real application, this would trigger an SMS service.
     * @param phoneNumber The target phone number in E.164 format.
     */
    @Override
    public void generateAndSend(String phoneNumber) {
        String otp = String.format("%06d", random.nextInt(999999));
        otpCache.put(phoneNumber, otp);
        // In a real application, you would use an SMS provider here.
        // E.g., smsProvider.send(phoneNumber, "Your verification code is: " + otp);
        log.info("Generated OTP for {}: {}", phoneNumber, otp);
    }

    /**
     * Verifies an OTP against the value in the cache.
     * @param phoneNumber The phone number associated with the OTP.
     * @param otp The 6-digit OTP string to verify.
     * @return true if the OTP matches and hasn't expired, false otherwise.
     */
    @Override
    public boolean verify(String phoneNumber, String otp) {
        String cachedOtp = otpCache.getIfPresent(phoneNumber);
        if (cachedOtp != null && cachedOtp.equals(otp)) {
            otpCache.invalidate(phoneNumber); // OTP is single-use
            return true;
        }
        return false;
    }
}