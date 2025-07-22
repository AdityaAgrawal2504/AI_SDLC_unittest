package com.example.auth.util;

/**
 * Contains constant values for validation rules, such as regular expressions and messages.
 */
public final class ValidationConstants_AuthVerifyOtp_17169 {

    private ValidationConstants_AuthVerifyOtp_17169() {
        // Private constructor to prevent instantiation
    }

    public static final String PHONE_NUMBER_REGEX = "^\\+[1-9]\\d{1,14}$";
    public static final String PHONE_NUMBER_MESSAGE = "Must be a non-empty string in the international E.164 format.";

    public static final String OTP_REGEX = "^\\d{6}$";
    public static final String OTP_MESSAGE = "Must be a non-empty string consisting of exactly 6 digits.";
}
src/main/java/com/example/auth/dto/request/VerifyOtpRequest_17169.java