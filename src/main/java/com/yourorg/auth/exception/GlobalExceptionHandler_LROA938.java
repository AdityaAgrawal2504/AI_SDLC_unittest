package com.yourorg.auth.exception;

import com.yourorg.auth.dto.error.ApiError_LROA938;
import com.yourorg.auth.dto.error.ErrorDetail_LROA938;
import com.yourorg.auth.enums.ErrorCode_LROA938;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Centralized exception handler to convert exceptions into standardized API error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler_LROA938 {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler_LROA938.class);

    /**
     * Handles custom application-specific exceptions.
     */
    @ExceptionHandler(ApplicationException_LROA938.class)
    public ResponseEntity<ApiError_LROA938> handleApplicationException(ApplicationException_LROA938 ex) {
        ErrorCode_LROA938 errorCode = ex.getErrorCode();
        logger.warn("ApplicationException caught: {}", errorCode.getCode(), ex);
        ApiError_LROA938 apiError = ApiError_LROA938.builder()
                .errorCode(errorCode.getCode())
                .errorMessage(errorCode.getMessage())
                .build();
        return new ResponseEntity<>(apiError, errorCode.getHttpStatus());
    }

    /**
     * Handles validation errors from @Valid annotation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError_LROA938> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorCode_LROA938 errorCode = ErrorCode_LROA938.VALIDATION_FAILED;
        List<ErrorDetail_LROA938> errorDetails = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorDetail_LROA938(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiError_LROA938 apiError = ApiError_LROA938.builder()
                .errorCode(errorCode.getCode())
                .errorMessage(errorCode.getMessage())
                .errorDetails(errorDetails)
                .build();
        logger.warn("Validation failed: {}", apiError);
        return new ResponseEntity<>(apiError, errorCode.getHttpStatus());
    }

    /**
     * Handles all other unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError_LROA938> handleGenericException(Exception ex) {
        ErrorCode_LROA938 errorCode = ErrorCode_LROA938.INTERNAL_SERVER_ERROR;
        logger.error("An unexpected error occurred", ex);
        ApiError_LROA938 apiError = ApiError_LROA938.builder()
                .errorCode(errorCode.getCode())
                .errorMessage(errorCode.getMessage())
                .build();
        return new ResponseEntity<>(apiError, errorCode.getHttpStatus());
    }
}
```
```java
// src/main/java/com/yourorg/auth/model/User_LROA938.java