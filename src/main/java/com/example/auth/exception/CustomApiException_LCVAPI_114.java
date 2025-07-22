package com.example.auth.exception;

import com.example.auth.enums.ErrorCode_LCVAPI_107;
import lombok.Getter;

/**
 * A custom, unchecked exception to represent application-specific errors.
 */
@Getter
public class CustomApiException_LCVAPI_114 extends RuntimeException {

    private final ErrorCode_LCVAPI_107 errorCode;

    /**
     * Constructs a new exception with a specific error code.
     * @param errorCode The error code enum.
     */
    public CustomApiException_LCVAPI_114(ErrorCode_LCVAPI_107 errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new exception with a specific error code and a custom message.
     * @param errorCode The error code enum.
     * @param message The custom detail message.
     */
    public CustomApiException_LCVAPI_114(ErrorCode_LCVAPI_107 errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
src/main/java/com/example/auth/exception/GlobalExceptionHandler_LCVAPI_115.java