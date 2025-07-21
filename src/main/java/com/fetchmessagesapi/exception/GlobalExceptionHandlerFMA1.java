package com.fetchmessagesapi.exception;

import com.fetchmessagesapi.dto.ErrorResponseFMA1;
import com.fetchmessagesapi.enums.ErrorCodeFMA1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;

/**
 * Global handler for exceptions, translating them into standardized error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandlerFMA1 {

    private static final Logger log = LogManager.getLogger(GlobalExceptionHandlerFMA1.class);

    /**
     * Handles custom API exceptions.
     */
    @ExceptionHandler(ApiBaseExceptionFMA1.class)
    public ResponseEntity<ErrorResponseFMA1> handleApiBaseException(ApiBaseExceptionFMA1 ex) {
        log.warn("API Exception: Status={}, Code={}, Message={}", ex.getStatus(), ex.getErrorCode(), ex.getMessage());
        ErrorResponseFMA1 errorResponse = new ErrorResponseFMA1(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    /**
     * Handles validation errors for request parameters.
     */
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponseFMA1> handleValidationException(Exception ex) {
        String message = "Invalid parameter supplied. Please check the request.";
        if (ex instanceof ConstraintViolationException cve) {
            message = cve.getConstraintViolations().iterator().next().getMessage();
        } else if (ex instanceof MethodArgumentNotValidException manve) {
            message = manve.getBindingResult().getFieldError().getDefaultMessage();
        } else if (ex instanceof MethodArgumentTypeMismatchException matme) {
            message = String.format("Parameter '%s' must be of type '%s'.", matme.getName(), matme.getRequiredType().getSimpleName());
        }
        log.warn("Validation Exception: {}", message, ex);
        ErrorResponseFMA1 errorResponse = new ErrorResponseFMA1(ErrorCodeFMA1.INVALID_PARAMETER, message);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseFMA1> handleGenericException(Exception ex) {
        log.error("An unexpected internal server error occurred.", ex);
        ErrorResponseFMA1 errorResponse = new ErrorResponseFMA1(ErrorCodeFMA1.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/repository/ConversationParticipantRepositoryFMA1.java