package com.example.userregistration.exception;

import com.example.userregistration.dto.ApiError_URAPI_1;
import com.example.userregistration.logging.StructuredLogger_URAPI_1;
import com.example.userregistration.model.ErrorCode_URAPI_1;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler to catch exceptions across the whole application.
 * It translates exceptions into standardized API error responses.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler_URAPI_1 {

    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler_URAPI_1.class);
    private final StructuredLogger_URAPI_1 structuredLogger;

    /**
     * Handles validation errors from @Valid annotation.
     * @param ex The MethodArgumentNotValidException thrown.
     * @return A ResponseEntity containing the ApiError DTO and a 400 Bad Request status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError_URAPI_1> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiError_URAPI_1 apiError = ApiError_URAPI_1.builder()
                .errorCode(ErrorCode_URAPI_1.VALIDATION_ERROR)
                .message("Input validation failed.")
                .details(errors)
                .build();
        
        log.warn("Validation error: {}", apiError);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the case where a user with the given phone number already exists.
     * @param ex The UserAlreadyExistsException thrown.
     * @return A ResponseEntity with the ApiError DTO and a 409 Conflict status.
     */
    @ExceptionHandler(UserAlreadyExistsException_URAPI_1.class)
    public ResponseEntity<ApiError_URAPI_1> handleUserAlreadyExistsException(UserAlreadyExistsException_URAPI_1 ex) {
        ApiError_URAPI_1 apiError = ApiError_URAPI_1.builder()
                .errorCode(ErrorCode_URAPI_1.USER_ALREADY_EXISTS)
                .message(ex.getMessage())
                .build();

        log.warn("Conflict error: {}", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    /**
     * Handles failures during the password hashing process.
     * @param ex The PasswordHashingException thrown.
     * @return A ResponseEntity with the ApiError DTO and a 500 Internal Server Error status.
     */
    @ExceptionHandler(PasswordHashingException_URAPI_1.class)
    public ResponseEntity<ApiError_URAPI_1> handlePasswordHashingException(PasswordHashingException_URAPI_1 ex) {
        ApiError_URAPI_1 apiError = ApiError_URAPI_1.builder()
                .errorCode(ErrorCode_URAPI_1.PASSWORD_HASHING_FAILURE)
                .message("An internal security error occurred.")
                .build();
        
        log.error("Password hashing failed.", ex);
        structuredLogger.error("Password Hashing Failure", ex, Map.of("reason", ex.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles database-related errors, like connectivity or constraint issues.
     * @param ex The DataAccessException thrown.
     * @return A ResponseEntity with the ApiError DTO and a 500 Internal Server Error status.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError_URAPI_1> handleDatabaseException(DataAccessException ex) {
        ApiError_URAPI_1 apiError = ApiError_URAPI_1.builder()
                .errorCode(ErrorCode_URAPI_1.DATABASE_ERROR)
                .message("A database error occurred.")
                .build();
        
        log.error("Database access error.", ex);
        structuredLogger.error("Database Access Failure", ex, Map.of("rootCause", ex.getRootCause() != null ? ex.getRootCause().getMessage() : "N/A"));
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * A catch-all handler for any other unhandled exceptions.
     * @param ex The generic Exception thrown.
     * @return A ResponseEntity with the ApiError DTO and a 500 Internal Server Error status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError_URAPI_1> handleGenericException(Exception ex) {
        ApiError_URAPI_1 apiError = ApiError_URAPI_1.builder()
                .errorCode(ErrorCode_URAPI_1.UNEXPECTED_ERROR)
                .message("An unexpected internal server error occurred.")
                .build();
        
        log.error("An unexpected error occurred.", ex);
        structuredLogger.error("Unexpected Generic Exception", ex, Map.of());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
src/test/java/com/example/userregistration/service/UserRegistrationService_URAPI_1Test.java
```java