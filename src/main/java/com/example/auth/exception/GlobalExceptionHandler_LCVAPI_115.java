package com.example.auth.exception;

import com.example.auth.dto.ErrorResponse_LCVAPI_106;
import com.example.auth.enums.ErrorCode_LCVAPI_107;
import com.example.auth.util.RequestIdUtil_LCVAPI_111;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

/**
 * Global exception handler to catch and format exceptions into a standard error response.
 */
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler_LCVAPI_115 {

    /**
     * Handles custom application-specific exceptions.
     * @param ex The caught CustomApiException.
     * @return A ResponseEntity containing the structured error.
     */
    @ExceptionHandler(CustomApiException_LCVAPI_114.class)
    public ResponseEntity<ErrorResponse_LCVAPI_106> handleCustomApiException(CustomApiException_LCVAPI_114 ex) {
        ErrorCode_LCVAPI_107 errorCode = ex.getErrorCode();
        ErrorResponse_LCVAPI_106 errorResponse = new ErrorResponse_LCVAPI_106(
                errorCode.name(),
                ex.getMessage(),
                OffsetDateTime.now(),
                RequestIdUtil_LCVAPI_111.get()
        );
        log.warn("API Error: [Code: {}, Status: {}, Message: {}] for Request ID: {}", errorCode.name(), errorCode.getHttpStatus().value(), ex.getMessage(), RequestIdUtil_LCVAPI_111.get());
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    /**
     * Handles bean validation exceptions from @Valid.
     * @param ex The caught MethodArgumentNotValidException.
     * @return A ResponseEntity containing the structured error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse_LCVAPI_106> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse_LCVAPI_106 errorResponse = new ErrorResponse_LCVAPI_106(
                ErrorCode_LCVAPI_107.INVALID_INPUT.name(),
                errorMessage,
                OffsetDateTime.now(),
                RequestIdUtil_LCVAPI_111.get()
        );
        log.warn("Validation Error: {} for Request ID: {}", errorMessage, RequestIdUtil_LCVAPI_111.get());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles any other unhandled exceptions as a fallback.
     * @param ex The caught Exception.
     * @return A ResponseEntity containing a generic internal server error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse_LCVAPI_106> handleAllExceptions(Exception ex) {
        ErrorCode_LCVAPI_107 errorCode = ErrorCode_LCVAPI_107.INTERNAL_SERVER_ERROR;
        ErrorResponse_LCVAPI_106 errorResponse = new ErrorResponse_LCVAPI_106(
                errorCode.name(),
                errorCode.getDefaultMessage(),
                OffsetDateTime.now(),
                RequestIdUtil_LCVAPI_111.get()
        );
        log.error("Unhandled Internal Server Error for Request ID: {}", RequestIdUtil_LCVAPI_111.get(), ex);
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }
}
src/main/java/com/example/auth/model/User_LCVAPI_109.java