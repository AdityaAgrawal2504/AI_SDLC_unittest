package com.example.controller;

import com.example.dto.ErrorResponse_EX_9002;
import com.example.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global exception handler to catch exceptions thrown from controllers and map them to HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler_EX_9001 {

    /**
     * Handles validation errors from @Valid annotation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse_EX_9002> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", "Validation failed: " + errors);
    }

    /**
     * Handles validation errors from @Validated annotation on path/query params.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse_EX_9002> handleConstraintViolationException(ConstraintViolationException ex) {
        String errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", "Validation failed: " + errors);
    }
    
    /**
     * Handles user already exists exception.
     */
    @ExceptionHandler(UserAlreadyExistsException_UATH_1010.class)
    public ResponseEntity<ErrorResponse_EX_9002> handleUserAlreadyExists(UserAlreadyExistsException_UATH_1010 ex) {
        return createErrorResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
    }

    /**
     * Handles invalid credentials exception for login initiation.
     */
    @ExceptionHandler(InvalidCredentialsException_UATH_1011.class)
    public ResponseEntity<ErrorResponse_EX_9002> handleInvalidCredentials(InvalidCredentialsException_UATH_1011 ex) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage());
    }

    /**
     * Handles OTP-related errors (invalid, expired, not found).
     */
    @ExceptionHandler({OtpValidationException_UATH_1012.class, OtpSessionNotFoundException_UATH_1013.class})
    public ResponseEntity<ErrorResponse_EX_9002> handleOtpExceptions(RuntimeException ex) {
        HttpStatus status = (ex instanceof OtpSessionNotFoundException_UATH_1013) ? HttpStatus.NOT_FOUND : HttpStatus.UNAUTHORIZED;
        String reason = (ex instanceof OtpSessionNotFoundException_UATH_1013) ? "Not Found" : "Unauthorized";
        return createErrorResponse(status, reason, ex.getMessage());
    }
    
    /**
     * Handles resource not found exceptions.
     */
    @ExceptionHandler({UserNotFoundException_UATH_1014.class, ConversationNotFoundException_CHAT_2010.class})
    public ResponseEntity<ErrorResponse_EX_9002> handleNotFoundExceptions(RuntimeException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    /**
     * Handles forbidden access exceptions.
     */
    @ExceptionHandler(ForbiddenAccessException_CHAT_2011.class)
    public ResponseEntity<ErrorResponse_EX_9002> handleForbiddenAccess(ForbiddenAccessException_CHAT_2011 ex) {
        return createErrorResponse(HttpStatus.FORBIDDEN, "Forbidden", ex.getMessage());
    }

    /**
     * Handles failed external service calls, like OTP provider.
     */
    @ExceptionHandler(OtpServiceException_UATH_1015.class)
    public ResponseEntity<ErrorResponse_EX_9002> handleOtpServiceUnavailable(OtpServiceException_UATH_1015 ex) {
        return createErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable", ex.getMessage());
    }

     /**
     * Handles generic Spring Security authentication exceptions.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse_EX_9002> handleAuthenticationException(AuthenticationException ex) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "Authentication failed: " + ex.getMessage());
    }

    /**
     * Catches any other unhandled exception.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse_EX_9002> handleGenericException(Exception ex) {
        // Log the full stack trace for internal debugging
        ex.printStackTrace();
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred on the server.");
    }

    /**
     * Helper to build a standard error response entity.
     */
    private ResponseEntity<ErrorResponse_EX_9002> createErrorResponse(HttpStatus status, String reason, String message) {
        ErrorResponse_EX_9002 errorResponse = new ErrorResponse_EX_9002(status.value(), reason, message);
        return new ResponseEntity<>(errorResponse, status);
    }
}
```
```java
//
// Filename: src/main/java/com/example/dto/AuthTokenResponse_UATH_1002.java
//