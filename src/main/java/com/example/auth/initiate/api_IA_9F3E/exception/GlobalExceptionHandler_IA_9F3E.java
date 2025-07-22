package com.example.auth.initiate.api_IA_9F3E.exception;

import com.example.auth.initiate.api_IA_9F3E.constants.ErrorCode_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.dto.ApiErrorResponseDTO_IA_9F3E;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler to catch and format exceptions into standard API error responses.
 */
@RestControllerAdvice(basePackages = "com.example.auth.initiate.api_IA_9F3E")
public class GlobalExceptionHandler_IA_9F3E extends ResponseEntityExceptionHandler {

    /**
     * Handles custom API exceptions (subclasses of BaseApiException).
     * @param ex The caught exception.
     * @return A response entity with a formatted error DTO.
     */
    @ExceptionHandler(BaseApiException_IA_9F3E.class)
    public ResponseEntity<ApiErrorResponseDTO_IA_9F3E> handleApiExceptions(BaseApiException_IA_9F3E ex) {
        ApiErrorResponseDTO_IA_9F3E errorResponse = ApiErrorResponseDTO_IA_9F3E.builder()
                .success(false)
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handles validation exceptions for request bodies.
     * @param ex The MethodArgumentNotValidException.
     * @param headers The HTTP headers.
     * @param status The HTTP status code.
     * @param request The current web request.
     * @return A response entity with formatted validation error details.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ApiErrorResponseDTO_IA_9F3E errorResponse = ApiErrorResponseDTO_IA_9F3E.builder()
                .success(false)
                .errorCode(ErrorCode_IA_9F3E.INVALID_INPUT)
                .message("Invalid input provided. Please check the fields.")
                .details(errors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles all other uncaught exceptions.
     * @param ex The caught exception.
     * @return A response entity for a generic internal server error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDTO_IA_9F3E> handleGenericException(Exception ex) {
        ApiErrorResponseDTO_IA_9F3E errorResponse = ApiErrorResponseDTO_IA_9F3E.builder()
                .success(false)
                .errorCode(ErrorCode_IA_9F3E.INTERNAL_SERVER_ERROR)
                .message("An unexpected error occurred. Please try again later.")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
```java