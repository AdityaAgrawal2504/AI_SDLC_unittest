package com.yourorg.fetchconversationsapi.exception;

import com.yourorg.fetchconversationsapi.dto.ApiErrorFCA8123;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.HashMap;
import java.util.Map;

/**
 * Global handler for exceptions across the application.
 * Converts exceptions into standardized API error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandlerFCA8123 {

    /**
     * Handles validation exceptions for request parameters.
     * @param ex The caught ConstraintViolationException.
     * @return A ResponseEntity with a 400 Bad Request status and a detailed error body.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorFCA8123> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> details = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            // Extract the simple field name from the full path
            String simpleFieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
            details.put(simpleFieldName, violation.getMessage());
        }

        ApiErrorFCA8123 apiError = ApiErrorFCA8123.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorCode("INVALID_PARAMETER")
                .message("The request contains invalid parameters.")
                .details(details)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions for mismatched argument types, e.g., providing a string for an enum.
     * @param ex The caught MethodArgumentTypeMismatchException.
     * @return A ResponseEntity with a 400 Bad Request status and a detailed error body.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorFCA8123> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("The parameter '%s' has an invalid value '%s'.", ex.getName(), ex.getValue());

        ApiErrorFCA8123 apiError = ApiErrorFCA8123.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorCode("INVALID_PARAMETER")
                .message(message)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catches any other unhandled exceptions as a last resort.
     * @param ex The caught Exception.
     * @return A ResponseEntity with a 500 Internal Server Error status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorFCA8123> handleGlobalException(Exception ex) {
        ApiErrorFCA8123 apiError = ApiErrorFCA8123.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCode("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred on the server.")
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
src/main/java/com/yourorg/fetchconversationsapi/config/SecurityConfigFCA8123.java
```java