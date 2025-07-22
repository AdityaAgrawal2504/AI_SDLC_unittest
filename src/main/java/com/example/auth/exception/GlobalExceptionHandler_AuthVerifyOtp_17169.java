package com.example.auth.exception;

import com.example.auth.dto.response.ErrorResponse_17169;
import com.example.auth.enums.ErrorCode_AuthVerifyOtp_17169;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Global exception handler to catch and format errors into a standard ErrorResponse DTO.
 */
@RestControllerAdvice
public class GlobalExceptionHandler_AuthVerifyOtp_17169 {

    /**
     * Handles custom application-specific exceptions.
     */
    @ExceptionHandler(ApplicationException_AuthVerifyOtp_17169.class)
    public ResponseEntity<ErrorResponse_17169> handleApplicationException(ApplicationException_AuthVerifyOtp_17169 ex) {
        ErrorCode_AuthVerifyOtp_17169 errorCode = ex.getErrorCode();
        ErrorResponse_17169 errorResponse = new ErrorResponse_17169(
                errorCode.getCode(),
                errorCode.getMessage(),
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    /**
     * Handles validation errors from @Valid annotation on request bodies.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse_17169> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String field = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getField();
        ErrorCode_AuthVerifyOtp_17169 errorCode;

        if ("phoneNumber".equals(field)) {
            errorCode = ErrorCode_AuthVerifyOtp_17169.INVALID_PHONE_FORMAT;
        } else if ("otp".equals(field)) {
            errorCode = ErrorCode_AuthVerifyOtp_17169.INVALID_OTP_FORMAT;
        } else {
            errorCode = ErrorCode_AuthVerifyOtp_17169.INVALID_INPUT_FORMAT;
        }

        ErrorResponse_17169 errorResponse = new ErrorResponse_17169(
                errorCode.getCode(),
                errorCode.getMessage(),
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    /**
     * Handles errors from malformed JSON requests.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse_17169> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ErrorCode_AuthVerifyOtp_17169 errorCode = ErrorCode_AuthVerifyOtp_17169.INVALID_INPUT_FORMAT;
        ErrorResponse_17169 errorResponse = new ErrorResponse_17169(
                errorCode.getCode(),
                errorCode.getMessage(),
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    /**
     * Handles any other unhandled exceptions as a fallback.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse_17169> handleGlobalException(Exception ex) {
        ErrorCode_AuthVerifyOtp_17169 errorCode = ErrorCode_AuthVerifyOtp_17169.INTERNAL_SERVER_ERROR;
        ErrorResponse_17169 errorResponse = new ErrorResponse_17169(
                errorCode.getCode(),
                errorCode.getMessage(),
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }
}
src/main/java/com/example/auth/controller/AuthController_17169.java