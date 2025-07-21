package com.yourorg.yourapp.exception.handler;

import com.yourorg.yourapp.dto.error.ApiErrorLROA9123;
import com.yourorg.yourapp.dto.error.ValidationErrorDetailLROA9123;
import com.yourorg.yourapp.enums.ErrorCodeLROA9123;
import com.yourorg.yourapp.exception.ApiExceptionLROA9123;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler to catch and format exceptions into standard ApiError responses.
 */
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandlerLROA9123 extends ResponseEntityExceptionHandler {

    /**
     * Handles custom API exceptions and maps them to the appropriate error response.
     */
    @ExceptionHandler(ApiExceptionLROA9123.class)
    public ResponseEntity<ApiErrorLROA9123> handleApiException(ApiExceptionLROA9123 ex) {
        ErrorCodeLROA9123 errorCode = ex.getErrorCode();
        log.warn("API Error Occurred: code={}, message='{}'", errorCode.getCode(), ex.getMessage());
        ApiErrorLROA9123 apiError = new ApiErrorLROA9123(errorCode.getCode(), ex.getMessage());
        return new ResponseEntity<>(apiError, errorCode.getHttpStatus());
    }

    /**
     * Handles validation exceptions from @Valid annotation.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.warn("Validation error: {}", ex.getMessage());
        List<ValidationErrorDetailLROA9123> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationErrorDetailLROA9123(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorCodeLROA9123 errorCode = ErrorCodeLROA9123.VALIDATION_FAILED;
        ApiErrorLROA9123 apiError = ApiErrorLROA9123.builder()
                .errorCode(errorCode.getCode())
                .errorMessage(errorCode.getDefaultMessage())
                .errorDetails(validationErrors)
                .build();
        return new ResponseEntity<>(apiError, errorCode.getHttpStatus());
    }
    
    /**
     * Handles any other unhandled exceptions, returning a generic internal server error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorLROA9123> handleAllUncaughtException(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred", ex);
        ErrorCodeLROA9123 errorCode = ErrorCodeLROA9123.INTERNAL_SERVER_ERROR;
        ApiErrorLROA9123 apiError = new ApiErrorLROA9123(errorCode.getCode(), errorCode.getDefaultMessage());
        return new ResponseEntity<>(apiError, errorCode.getHttpStatus());
    }
}
```
src/main/java/com/yourorg/yourapp/config/SecurityConfigLROA9123.java
```java