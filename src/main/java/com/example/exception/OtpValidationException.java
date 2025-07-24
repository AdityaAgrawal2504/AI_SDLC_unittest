package com.example.exception;

import org.springframework.http.HttpStatus;

public class OtpValidationException extends AppException {
    public OtpValidationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "OTP_VALIDATION_FAILED");
    }
}