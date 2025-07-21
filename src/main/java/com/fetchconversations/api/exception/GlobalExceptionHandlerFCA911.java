package com.fetchconversations.api.exception;

import com.fetchconversations.api.dto.ApiErrorFCA911;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles exceptions globally across the application, formatting them into a standard API error response.
 */
@RestControllerAdvice
public class GlobalExceptionHandlerFCA911 {

    /**
     * Handles validation exceptions for request parameters.
     * @param ex The caught ConstraintViolationException.
     * @return A ResponseEntity with a 400 Bad Request status and detailed error info.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorFCA911> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> details = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            // Extracts 'page' from 'fetchConversations.page'
            String field = violation.getPropertyPath().toString().replaceAll(".*\\.", "");
            details.put(field, violation.getMessage());
        }

        ApiErrorFCA911 error = ApiErrorFCA911.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorCode("INVALID_PARAMETER")
                .message("Input validation failed.")
                .details(details)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions when a request parameter cannot be converted to the target type, e.g., invalid enum value.
     * @param ex The caught MethodArgumentTypeMismatchException.
     * @return A ResponseEntity with a 400 Bad Request status and detailed error info.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorFCA911> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("The parameter '%s' has an invalid value '%s'.", ex.getName(), ex.getValue());
        ApiErrorFCA911 error = ApiErrorFCA911.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorCode("INVALID_PARAMETER")
                .message(message)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles generic exceptions to prevent unhandled errors from reaching the client.
     * @param ex The caught Exception.
     * @return A ResponseEntity with a 500 Internal Server Error status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorFCA911> handleGenericException(Exception ex) {
        ApiErrorFCA911 error = ApiErrorFCA911.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCode("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred on the server.")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
```java
// src/main/java/com/fetchconversations/api/controller/ConversationControllerFCA911.java