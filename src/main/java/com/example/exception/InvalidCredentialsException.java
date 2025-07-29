src/main/java/com/example/exception/InvalidCredentialsException.java
package com.example.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for invalid login credentials or OTP.
 */
public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException(String message) {
        super(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", message);
    }
}