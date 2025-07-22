package com.example.exception;

import com.example.dto.ApiError_F4B8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler to catch exceptions and format them into a standard ApiError response.
 */
@RestControllerAdvice
public class GlobalExceptionHandler_F4B8 {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler_F4B8.class);

    /**
     * Handles input validation errors (JSR-303).
     * @param ex The MethodArgumentNotValidException instance.
     * @return A ResponseEntity with a 400 Bad Request status and detailed error information.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError_F4B8> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiError_F4B8 apiError = new ApiError_F4B8(
            ErrorCode_F4B8.VALIDATION_ERROR.name(),
            "Input validation failed.",
            errors);
        log.warn("Validation error: {}", apiError);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles conflicts when a user already exists.
     * @param ex The UserAlreadyExistsException_F4B8 instance.
     * @return A ResponseEntity with a 409 Conflict status.
     */
    @ExceptionHandler(UserAlreadyExistsException_F4B8.class)
    public ResponseEntity<ApiError_F4B8> handleUserAlreadyExistsException(UserAlreadyExistsException_F4B8 ex) {
        ApiError_F4B8 apiError = new ApiError_F4B8(
            ErrorCode_F4B8.USER_ALREADY_EXISTS.name(),
            ex.getMessage());
        log.warn("Conflict error: {}", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
    
    /**
     * Handles failures during the password hashing process.
     * @param ex The PasswordHashingException_F4B8 instance.
     * @return A ResponseEntity with a 500 Internal Server Error status.
     */
    @ExceptionHandler(PasswordHashingException_F4B8.class)
    public ResponseEntity<ApiError_F4B8> handlePasswordHashingException(PasswordHashingException_F4B8 ex) {
        ApiError_F4B8 apiError = new ApiError_F4B8(
            ErrorCode_F4B8.PASSWORD_HASHING_FAILURE.name(),
            "Could not process registration due to a security configuration error.");
        log.error("Password hashing failed.", ex);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles errors related to database access.
     * @param ex The DataAccessException instance.
     * @return A ResponseEntity with a 500 Internal Server Error status.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError_F4B8> handleDatabaseException(DataAccessException ex) {
        ApiError_F4B8 apiError = new ApiError_F4B8(
            ErrorCode_F4B8.DATABASE_ERROR.name(),
            "A database error occurred.");
        log.error("Database access error.", ex);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * Handles all other unexpected exceptions as a fallback.
     * @param ex The Exception instance.
     * @return A ResponseEntity with a 500 Internal Server Error status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError_F4B8> handleGlobalException(Exception ex) {
        ApiError_F4B8 apiError = new ApiError_F4B8(
            ErrorCode_F4B8.UNEXPECTED_ERROR.name(),
            "An unexpected internal server error occurred.");
        log.error("An unexpected error occurred.", ex);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
```java
src/main/java/com/example/config/WebSecurityConfig_F4B8.java