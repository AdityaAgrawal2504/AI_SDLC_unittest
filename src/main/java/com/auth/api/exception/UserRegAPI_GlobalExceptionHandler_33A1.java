package com.auth.api.exception;

import com.auth.api.dto.UserRegAPI_ApiError_33A1;
import com.auth.api.enums.UserRegAPI_ErrorCode_33A1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler to catch and format exceptions into standard API error responses.
 */
@RestControllerAdvice
public class UserRegAPI_GlobalExceptionHandler_33A1 {

    private static final Logger logger = LogManager.getLogger(UserRegAPI_GlobalExceptionHandler_33A1.class);

    /**
     * Handles validation exceptions from @Valid.
     * @param ex The MethodArgumentNotValidException instance.
     * @return A ResponseEntity containing the formatted validation error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UserRegAPI_ApiError_33A1> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        UserRegAPI_ApiError_33A1 apiError = new UserRegAPI_ApiError_33A1(
            UserRegAPI_ErrorCode_33A1.VALIDATION_ERROR,
            "One or more fields failed validation."
        );
        apiError.setDetails(errors);

        logger.warn("Validation error: {}", errors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles conflicts when a user already exists.
     * @param ex The UserRegAPI_ConflictException_33A1 instance.
     * @return A ResponseEntity with a 409 Conflict status.
     */
    @ExceptionHandler(UserRegAPI_ConflictException_33A1.class)
    public ResponseEntity<UserRegAPI_ApiError_33A1> handleConflictException(UserRegAPI_ConflictException_33A1 ex) {
        UserRegAPI_ApiError_33A1 apiError = new UserRegAPI_ApiError_33A1(
            UserRegAPI_ErrorCode_33A1.USER_ALREADY_EXISTS,
            ex.getMessage()
        );
        logger.warn("Conflict error: {}", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    /**
     * Handles all other unexpected exceptions.
     * @param ex The Exception instance.
     * @return A ResponseEntity with a 500 Internal Server Error status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserRegAPI_ApiError_33A1> handleAllExceptions(Exception ex) {
        UserRegAPI_ApiError_33A1 apiError = new UserRegAPI_ApiError_33A1(
            UserRegAPI_ErrorCode_33A1.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred on the server."
        );
        logger.error("Internal Server Error: ", ex);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
```java
// src/test/java/com/auth/api/service/UserRegAPI_AuthServiceImpl_33A1Test.java