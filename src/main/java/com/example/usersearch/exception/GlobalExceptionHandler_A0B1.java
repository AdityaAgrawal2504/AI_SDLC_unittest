package com.example.usersearch.exception;

import com.example.usersearch.dto.ErrorResponse_A0B1;
import com.example.usersearch.enums.ErrorCode_A0B1;
import com.example.usersearch.logging.LoggerService_A0B1;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global handler for application-wide exceptions.
 * Converts exceptions into standardized ErrorResponse_A0B1 DTOs.
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class GlobalExceptionHandler_A0B1 {

    private final LoggerService_A0B1 loggerService;
    private final ObjectMapper objectMapper;

    /**
     * Handles bean validation exceptions for request parameters.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse_A0B1> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
            .collect(Collectors.toMap(
                violation -> violation.getPropertyPath().toString(),
                ConstraintViolation::getMessage
            ));

        String firstViolationMessage = ex.getConstraintViolations().iterator().next().getMessage();
        ErrorCode_A0B1 errorCode = mapMessageToErrorCode(firstViolationMessage);

        ErrorResponse_A0B1 errorResponse = new ErrorResponse_A0B1(errorCode.getCode(), firstViolationMessage);
        errorResponse.setDetails(new HashMap<>(errors));

        loggerService.error("Validation error", ex, Map.of("details", errorResponse.getDetails()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles missing 'q' parameter specifically.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse_A0B1> handleMissingParams(MissingServletRequestParameterException ex) {
        if ("q".equals(ex.getParameterName())) {
            ErrorResponse_A0B1 errorResponse = new ErrorResponse_A0B1(ErrorCode_A0B1.VALIDATION_QUERY_MISSING);
            loggerService.error("Missing required parameter", ex, Map.of("parameter", "q"));
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return handleGenericException(ex);
    }

    /**
     * Handles custom database search exceptions.
     */
    @ExceptionHandler(DatabaseSearchException_A0B1.class)
    public ResponseEntity<ErrorResponse_A0B1> handleDatabaseSearchException(DatabaseSearchException_A0B1 ex) {
        ErrorResponse_A0B1 errorResponse = new ErrorResponse_A0B1(ex.getErrorCode());
        loggerService.error("Database search failed", ex, null);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles JWT and other authentication exceptions from the security filter chain.
     */
    public void handleAuthenticationException(AuthenticationException authException, HttpServletResponse response) throws IOException {
        ErrorCode_A0B1 errorCode = ErrorCode_A0B1.AUTH_TOKEN_INVALID;
        // Check for specific causes if needed
        if (authException.getCause() instanceof ExpiredJwtException) {
             log.warn("Authentication failed: JWT expired.");
        } else if (authException.getCause() instanceof JwtException) {
            log.warn("Authentication failed: JWT is invalid.");
        } else {
            log.warn("Authentication failed: {}", authException.getMessage());
        }

        ErrorResponse_A0B1 errorResponse = new ErrorResponse_A0B1(errorCode);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }


    /**
     * Handles all other un-caught exceptions as a fallback.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse_A0B1> handleGenericException(Exception ex) {
        ErrorResponse_A0B1 errorResponse = new ErrorResponse_A0B1(ErrorCode_A0B1.UNEXPECTED_SERVER_ERROR);
        loggerService.error("An unexpected error occurred", ex, null);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maps a validation message to a specific ErrorCode enumeration.
     */
    private ErrorCode_A0B1 mapMessageToErrorCode(String message) {
        if (message.contains("must be between 3 and 100")) {
            return message.contains("3") ? ErrorCode_A0B1.VALIDATION_QUERY_TOO_SHORT : ErrorCode_A0B1.VALIDATION_QUERY_TOO_LONG;
        } else if (message.contains("positive integer")) {
            return ErrorCode_A0B1.VALIDATION_INVALID_PAGE_NUMBER;
        } else if (message.contains("Page size")) {
            return ErrorCode_A0B1.VALIDATION_INVALID_PAGE_SIZE;
        } else if (message.contains("'q' query parameter is required")) {
            return ErrorCode_A0B1.VALIDATION_QUERY_MISSING;
        }
        return ErrorCode_A0B1.UNEXPECTED_SERVER_ERROR; // Fallback
    }
}
```
src/main/java/com/example/usersearch/config/OpenApiConfig_A0B1.java
```java