package com.yourorg.fetchmessagesapi.exception.fma1;

import com.yourorg.fetchmessagesapi.model.enums.fma1.ErrorCodeFMA_1;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandlerFMA_1 {

    /**
     * Handles custom API exceptions thrown from the application.
     */
    @ExceptionHandler(CustomApiExceptionFMA_1.class)
    public ResponseEntity<ErrorResponseFMA_1> handleCustomApiException(CustomApiExceptionFMA_1 ex) {
        log.warn("API Error: code={}, message='{}'", ex.getErrorCode().getCode(), ex.getMessage());
        ErrorResponseFMA_1 errorResponse = new ErrorResponseFMA_1(ex.getErrorCode().getCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handles validation errors from @Valid annotations.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseFMA_1> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("Parameter '%s' %s", error.getField(), error.getDefaultMessage()))
                .findFirst()
                .orElse(ex.getMessage());
        log.warn("Validation Error: {}", errorMessage);
        ErrorResponseFMA_1 errorResponse = new ErrorResponseFMA_1(ErrorCodeFMA_1.INVALID_PARAMETER.getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, ErrorCodeFMA_1.INVALID_PARAMETER.getStatus());
    }

    /**
     * Handles type mismatch errors for path/query parameters (e.g., invalid UUID format).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseFMA_1> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("Parameter '%s' has an invalid format. Expected type: %s.",
                ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown");
        log.warn("Type Mismatch Error: {}", errorMessage);
        ErrorResponseFMA_1 errorResponse = new ErrorResponseFMA_1(ErrorCodeFMA_1.INVALID_PARAMETER.getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, ErrorCodeFMA_1.INVALID_PARAMETER.getStatus());
    }

    /**
     * Catches all other unhandled exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseFMA_1> handleAllUncaughtException(Exception ex) {
        log.error("An unexpected error occurred", ex);
        ErrorResponseFMA_1 errorResponse = new ErrorResponseFMA_1(ErrorCodeFMA_1.INTERNAL_SERVER_ERROR.getCode(), "An unexpected internal error occurred.");
        return new ResponseEntity<>(errorResponse, ErrorCodeFMA_1.INTERNAL_SERVER_ERROR.getStatus());
    }
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/config/fma1/PaginationConstantsFMA_1.java