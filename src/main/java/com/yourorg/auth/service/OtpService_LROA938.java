package com.yourorg.auth.service;

/**
 * Interface for a service that generates and sends One-Time Passwords (OTPs).
 */
public interface OtpService_LROA938 {

    /**
     * Generates and sends an OTP to the specified phone number.
     * @param phone The E.164 formatted phone number to send the OTP to.
     */
    void sendOtp(String phone);
}
```
```java
// src/main/java/com/yourorg/auth/service/impl/OtpServiceImpl_LROA938.java