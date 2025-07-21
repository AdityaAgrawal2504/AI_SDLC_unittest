package com.yourorg.auth.constants;

/**
 * Holds constant values used across the application for the Login & Request OTP API.
 */
public final class ApiConstants_LROA938 {

    private ApiConstants_LROA938() {
        // Private constructor to prevent instantiation
    }

    public static final String PHONE_PATTERN = "^\\+[1-9]\\d{1,14}$";
    public static final String OTP_SENT_SUCCESSFULLY = "OTP has been sent successfully. Please check your device.";
    public static final String SUCCESS_STATUS = "success";
    public static final String OTP_VERIFY_SCOPE = "otp_verify";
}
```
```java
// src/main/java/com/yourorg/auth/dto/request/LoginRequest_LROA938.java