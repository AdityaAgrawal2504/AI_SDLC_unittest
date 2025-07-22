package com.example.auth.initiate.api_IA_9F3E.service;

/**
 * Service contract for generating and sending One-Time Passwords.
 */
public interface IOtpService_IA_9F3E {

    /**
     * Generates and sends an OTP to the specified phone number.
     * @param phoneNumber The recipient's phone number.
     * @param transactionId The unique ID correlating the login attempt.
     */
    void sendLoginOtp(String phoneNumber, String transactionId);
}
```
```java