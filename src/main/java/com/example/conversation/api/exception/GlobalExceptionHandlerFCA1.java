package com.example.conversation.api.exception;

import com.example.conversation.api.dto.ErrorResponseFCA1;
import com.example.conversation.api.dto.ValidationErrorDetailFCA1;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler to catch exceptions and convert them into standardized ErrorResponseFCA1 objects.
 */
@RestControllerAdvice(basePackages = "com.example.conversation.api")
public class GlobalExceptionHandlerFCA1 {

    /**
     * Handles validation exceptions from @Valid annotations on request bodies.
     *
     * @param ex The caught exception.
     * @return A ResponseEntity with a 400 Bad Request status and detailed error response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseFCA1> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ValidationErrorDetailFCA1> details = ex.getBindingResult().getAllErrors().stream()
            .map(error -> {
                String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : error.getObjectName();
                String errorMessage = error.getDefaultMessage();
                return new ValidationErrorDetailFCA1(fieldName, errorMessage);
            })
            .collect(Collectors.toList());

        ErrorResponseFCA1 errorResponse = new ErrorResponseFCA1("INVALID_INPUT", "Input validation failed.", details);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles validation exceptions from @Validated annotations on controller method parameters.
     *
     * @param ex The caught exception.
     * @return A ResponseEntity with a 400 Bad Request status and detailed error response.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseFCA1> handleConstraintViolation(ConstraintViolationException ex) {
        List<ValidationErrorDetailFCA1> details = ex.getConstraintViolations().stream()
            .map(violation -> {
                String fieldName = getFieldNameFromPath(violation);
                String errorMessage = violation.getMessage();
                return new ValidationErrorDetailFCA1(fieldName, errorMessage);
            })
            .collect(Collectors.toList());
        ErrorResponseFCA1 errorResponse = new ErrorResponseFCA1("INVALID_INPUT", "Input validation failed.", details);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles all other unexpected exceptions.
     *
     * @param ex      The caught exception.
     * @param request The current web request.
     * @return A ResponseEntity with a 500 Internal Server Error status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseFCA1> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponseFCA1 errorResponse = new ErrorResponseFCA1(
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred while processing the request.",
            null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Extracts the field name from a constraint violation path.
     *
     * @param violation The constraint violation.
     * @return The simple field name.
     */
    private String getFieldNameFromPath(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        // The path for a method parameter is typically "methodName.parameterName".
        // We just want the parameter name.
        if (path.contains(".")) {
            return path.substring(path.lastIndexOf('.') + 1);
        }
        return path; // Fallback if no dot found, though unlikely for method params
    }
}
src/main/java/com.example/conversation/api/security/SecurityUtilFCA1.java