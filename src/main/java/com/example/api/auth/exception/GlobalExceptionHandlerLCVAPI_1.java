package com.example.api.auth.exception;

import com.example.api.auth.dto.ErrorResponseLCVAPI_1;
import com.example.util.RequestIdUtilLCVAPI_1;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler to catch and format exceptions into a standard ErrorResponse.
 * This ensures all API errors, from validation to internal server errors, are consistent.
 */
@RestControllerAdvice
public class GlobalExceptionHandlerLCVAPI_1 {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandlerLCVAPI_1.class);

    /**
     * Handles custom application-specific exceptions.
     * @param ex The caught ApiException.
     * @return A ResponseEntity containing the standardized error response.
     */
    @ExceptionHandler(ApiExceptionLCVAPI_1.class)
    public ResponseEntity<ErrorResponseLCVAPI_1> handleApiException(ApiExceptionLCVAPI_1 ex) {
        ErrorCodeLCVAPI_1 errorCode = ex.getErrorCode();
        logger.warn("API Exception caught: code={}, message='{}'", errorCode.getCode(), ex.getMessage());
        ErrorResponseLCVAPI_1 errorResponse = new ErrorResponseLCVAPI_1(
            errorCode.getCode(),
            ex.getMessage(),
            ZonedDateTime.now(),
            RequestIdUtilLCVAPI_1.getRequestId()
        );
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    /**
     * Handles validation exceptions from @Valid annotations.
     * @param ex The caught MethodArgumentNotValidException.
     * @return A ResponseEntity containing the standardized error response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseLCVAPI_1> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorCodeLCVAPI_1 errorCode = ErrorCodeLCVAPI_1.INVALID_INPUT;
        String errors = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        String errorMessage = "Validation failed: " + errors;

        logger.warn("Validation Exception caught: {}", errorMessage);
        
        ErrorResponseLCVAPI_1 errorResponse = new ErrorResponseLCVAPI_1(
            errorCode.getCode(),
            errorMessage,
            ZonedDateTime.now(),
            RequestIdUtilLCVAPI_1.getRequestId()
        );
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    /**
     * Handles all other uncaught exceptions as a last resort.
     * @param ex The caught generic Exception.
     * @return A ResponseEntity for an internal server error.
     */
    @ExceptionHandler(Exception.C
```

src/main/java/com/example/api/auth/domain/UserLCVAPI_1.java
```java