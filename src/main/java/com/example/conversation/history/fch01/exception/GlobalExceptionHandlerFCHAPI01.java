package com.example.conversation.history.fch01.exception;

import com.example.conversation.history.fch01.dto.ApiErrorFCHAPI01;
import com.example.conversation.history.fch01.dto.ValidationErrorDetailFCHAPI01;
import com.example.conversation.history.fch01.enums.ErrorCodeFCHAPI01;
import com.example.conversation.history.fch01.logging.LoggableFCHAPI04;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler to catch exceptions and format them into a standard ApiError response.
 */
@RestControllerAdvice
@LoggableFCHAPI04
public class GlobalExceptionHandlerFCHAPI01 {

    /**
     * Handles 404 Not Found errors.
     */
    @ExceptionHandler(ResourceNotFoundExceptionFCHAPI01.class)
    public ResponseEntity<ApiErrorFCHAPI01> handleResourceNotFoundException(ResourceNotFoundExceptionFCHAPI01 ex) {
        ApiErrorFCHAPI01 error = ApiErrorFCHAPI01.builder()
                .errorCode(ex.getErrorCode().name())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, ex.getErrorCode().getStatus());
    }

    /**
     * Handles 403 Forbidden errors.
     */
    @ExceptionHandler(PermissionDeniedExceptionFCHAPI01.class)
    public ResponseEntity<ApiErrorFCHAPI01> handlePermissionDeniedException(PermissionDeniedExceptionFCHAPI01 ex) {
        ApiErrorFCHAPI01 error = ApiErrorFCHAPI01.builder()
                .errorCode(ex.getErrorCode().name())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, ex.getErrorCode().getStatus());
    }

    /**
     * Handles custom 400 Bad Request errors.
     */
    @ExceptionHandler(InvalidParameterExceptionFCHAPI01.class)
    public ResponseEntity<ApiErrorFCHAPI01> handleInvalidParameterException(InvalidParameterExceptionFCHAPI01 ex) {
        ApiErrorFCHAPI01 error = ApiErrorFCHAPI01.builder()
                .errorCode(ErrorCodeFCHAPI01.INVALID_PARAMETER.name())
                .message(ex.getMessage())
                .details(ex.getDetails())
                .build();
        return new ResponseEntity<>(error, ErrorCodeFCHAPI01.INVALID_PARAMETER.getStatus());
    }

    /**
     * Handles validation failures for @RequestParam, @PathVariable.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorFCHAPI01> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ValidationErrorDetailFCHAPI01> details = ex.getConstraintViolations().stream()
                .map(this::toValidationErrorDetail)
                .collect(Collectors.toList());
        ApiErrorFCHAPI01 error = ApiErrorFCHAPI01.builder()
                .errorCode(ErrorCodeFCHAPI01.INVALID_PARAMETER.name())
                .message(ErrorCodeFCHAPI01.INVALID_PARAMETER.getDefaultMessage())
                .details(details)
                .build();
        return new ResponseEntity<>(error, ErrorCodeFCHAPI01.INVALID_PARAMETER.getStatus());
    }

    /**
     * Handles validation failures for @RequestBody.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorFCHAPI01> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ValidationErrorDetailFCHAPI01> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationErrorDetailFCHAPI01(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        ApiErrorFCHAPI01 error = ApiErrorFCHAPI01.builder()
                .errorCode(ErrorCodeFCHAPI01.INVALID_PARAMETER.name())
                .message(ErrorCodeFCHAPI01.INVALID_PARAMETER.getDefaultMessage())
                .details(details)
                .build();
        return new ResponseEntity<>(error, ErrorCodeFCHAPI01.INVALID_PARAMETER.getStatus());
    }

    /**
     * Handles type mismatches in request parameters (e.g., string for an int).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorFCHAPI01> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String fieldName = ex.getName();
        String issue = String.format("Invalid value for parameter '%s'. Expected type '%s'.", fieldName, ex.getRequiredType().getSimpleName());
        ValidationErrorDetailFCHAPI01 detail = new ValidationErrorDetailFCHAPI01(fieldName, issue);
        ApiErrorFCHAPI01 error = ApiErrorFCHAPI01.builder()
                .errorCode(ErrorCodeFCHAPI01.INVALID_PARAMETER.name())
                .message(ErrorCodeFCHAPI01.INVALID_PARAMETER.getDefaultMessage())
                .details(List.of(detail))
                .build();
        return new ResponseEntity<>(error, ErrorCodeFCHAPI01.INVALID_PARAMETER.getStatus());
    }

    /**
     * Generic handler for all other unhandled exceptions, returning a 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorFCHAPI01> handleAllOtherExceptions(Exception ex) {
        ApiErrorFCHAPI01 error = ApiErrorFCHAPI01.builder()
                .errorCode(ErrorCodeFCHAPI01.INTERNAL_SERVER_ERROR.name())
                .message(ErrorCodeFCHAPI01.INTERNAL_SERVER_ERROR.getDefaultMessage())
                .build();
        return new ResponseEntity<>(error, ErrorCodeFCHAPI01.INTERNAL_SERVER_ERROR.getStatus());
    }
    
    private ValidationErrorDetailFCHAPI01 toValidationErrorDetail(ConstraintViolation<?> violation) {
        // Extracts the last part of the property path to get the field name.
        String path = violation.getPropertyPath().toString();
        String field = path.substring(path.lastIndexOf('.') + 1);
        return new ValidationErrorDetailFCHAPI01(field, violation.getMessage());
    }
}
src/main/java/com/example/conversation/history/fch01/service/ConversationServiceFCHAPI01.java