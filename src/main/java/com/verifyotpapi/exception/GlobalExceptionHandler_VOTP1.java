package com.verifyotpapi.exception;

import com.verifyotpapi.dto.response.ApiErrorResponse_VOTP1;
import com.verifyotpapi.util.ErrorCode_VOTP1;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralized exception handler for the application.
 */
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler_VOTP1 {

    /**
     * Handles custom API exceptions and returns a structured error response.
     */
    @ExceptionHandler(ApiException_VOTP1.class)
    public ResponseEntity<ApiErrorResponse_VOTP1> handleApiException(ApiException_VOTP1 ex) {
        log.warn("API Exception occurred: {}", ex.getErrorCode().getCode(), ex);
        ApiErrorResponse_VOTP1 errorResponse = new ApiErrorResponse_VOTP1(ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }

    /**
     * Handles validation exceptions from @Valid annotation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse_VOTP1> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());
        ApiErrorResponse_VOTP1 errorResponse = new ApiErrorResponse_VOTP1(ErrorCode_VOTP1.VALIDATION_ERROR);
        return new ResponseEntity<>(errorResponse, ErrorCode_VOTP1.VALIDATION_ERROR.getHttpStatus());
    }

    /**
     * Catches any other unhandled exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse_VOTP1> handleGenericException(Exception ex) {
        log.error("An unexpected internal server error occurred.", ex);
        ApiErrorResponse_VOTP1 errorResponse = new ApiErrorResponse_VOTP1(ErrorCode_VOTP1.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, ErrorCode_VOTP1.INTERNAL_SERVER_ERROR.getHttpStatus());
    }
}
```
```java
// src/main/java/com/verifyotpapi/model/OtpData_VOTP1.java