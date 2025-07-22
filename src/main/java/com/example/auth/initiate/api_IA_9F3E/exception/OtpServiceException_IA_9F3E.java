package com.example.auth.initiate.api_IA_9F3E.exception;

import com.example.auth.initiate.api_IA_9F3E.constants.ErrorCode_IA_9F3E;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when the OTP service fails.
 */
public class OtpServiceException_IA_9F3E extends BaseApiException_IA_9F3E {

    private static final String DEFAULT_MESSAGE = "Failed to send OTP. Please try again in a few minutes.";
    
    /**
     * Constructs an OtpServiceException with a default message.
     */
    public OtpServiceException_IA_9F3E() {
        super(DEFAULT_MESSAGE, HttpStatus.SERVICE_UNAVAILABLE, ErrorCode_IA_9F3E.OTP_SERVICE_UNAVAILABLE);
    }

    /**
     * Constructs an OtpServiceException with a custom message and cause.
     * @param message The detail message.
     * @param cause The cause of the exception.
     */
    public OtpServiceException_IA_9F3E(String message, Throwable cause) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE, ErrorCode_IA_9F3E.OTP_SERVICE_UNAVAILABLE);
        initCause(cause);
    }
}
```
```java