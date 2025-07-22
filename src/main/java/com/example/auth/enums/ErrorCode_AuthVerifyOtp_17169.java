package com.example.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enumeration of machine-readable error codes and their associated HTTP status and messages.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode_AuthVerifyOtp_17169 {

    // 400 Bad Request
    INVALID_INPUT_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_INPUT_FORMAT", "The request body is malformed or missing required fields."),
    INVALID_PHONE_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_PHONE_FORMAT", "The provided phone number is not in a valid format."),
    INVALID_OTP_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_OTP_FORMAT", "The provided OTP does not have the correct format."),

    // 401 Unauthorized
    OTP_INCORRECT(HttpStatus.UNAUTHORIZED, "OTP_INCORRECT", "The One-Time Password provided is incorrect."),
    OTP_EXPIRED(HttpStatus.UNAUTHORIZED, "OTP_EXPIRED", "The One-Time Password has expired. Please request a new one."),

    // 404 Not Found
    OTP_NOT_FOUND(HttpStatus.NOT_FOUND, "OTP_NOT_FOUND", "No pending OTP verification was found for this phone number. Please request a new OTP."),

    // 429 Too Many Requests
    TOO_MANY_ATTEMPTS(HttpStatus.TOO_MANY_REQUESTS, "TOO_MANY_ATTEMPTS", "Too many incorrect verification attempts. Please try again later."),

    // 500 Internal Server Error
    SESSION_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SESSION_CREATION_FAILED", "Could not create a user session. Please try again."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "An unexpected error occurred on the server.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
src/main/java/com/example/auth/exception/ApplicationException_AuthVerifyOtp_17169.java