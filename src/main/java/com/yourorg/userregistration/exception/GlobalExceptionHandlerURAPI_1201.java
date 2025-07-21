package com.yourorg.userregistration.exception;

import com.yourorg.userregistration.dto.ApiErrorURAPI_1201;
import com.yourorg.userregistration.enums.ErrorCodeURAPI_1201;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global handler for exceptions thrown by controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandlerURAPI_1201 {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandlerURAPI_1201.class);


    /**
     * Handles custom REST exceptions.
     * @param ex The custom exception instance.
     * @return A response entity with a formatted API error.
     */
    @ExceptionHandler(AbstractRestExceptionURAPI_1201.class)
    public ResponseEntity<ApiErrorURAPI_1201> handleRestException(AbstractRestExceptionURAPI_1201 ex) {
        ApiErrorURAPI_1201 apiError = new ApiErrorURAPI_1201(ex.getErrorCode().name(), ex.getMessage());
        logger.warn("REST Exception: Status={}, Code={}, Message={}", ex.getHttpStatus(), ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(apiError, ex.getHttpStatus());
    }

    /**
     * Handles validation exceptions from @Valid.
     * @param ex The validation exception.
     * @return A response entity with detailed validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorURAPI_1201> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiErrorURAPI_1201 apiError = new ApiErrorURAPI_1201(
                ErrorCodeURAPI_1201.VALIDATION_ERROR.name(),
                "One or more fields failed validation.",
                errors);
        logger.warn("Validation failed: {}", errors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other uncaught exceptions.
     * @param ex The generic exception.
     * @return A response entity for an internal server error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorURAPI_1201> handleAllExceptions(Exception ex) {
        ApiErrorURAPI_1201 apiError = new ApiErrorURAPI_1201(
                ErrorCodeURAPI_1201.INTERNAL_SERVER_ERROR.name(),
                "An unexpected error occurred on the server.");
        logger.error("Internal Server Error: ", ex);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
```java
// src/test/java/com/yourorg/userregistration/service/AuthServiceURAPI_1201Test.java