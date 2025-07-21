package com.yourorg.verifyotp.exception;

import com.yourorg.verifyotp.constants.ErrorCodeVAPI_1;
import com.yourorg.verifyotp.dto.ApiErrorResponseVAPI_1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandlerVAPI_1 extends ResponseEntityExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandlerVAPI_1.class);

    /**
     * Handles custom application-specific exceptions.
     */
    @ExceptionHandler(CustomApiExceptionVAPI_1.class)
    public ResponseEntity<ApiErrorResponseVAPI_1> handleCustomApiException(CustomApiExceptionVAPI_1 ex) {
        ApiErrorResponseVAPI_1 errorResponse = new ApiErrorResponseVAPI_1(ex.getErrorCode(), ex.getMessage());
        logger.warn("API Error: status={}, code={}, message='{}'", ex.getStatus(), ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    /**
     * Handles validation errors for request bodies.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorCodeVAPI_1 errorCode = ErrorCodeVAPI_1.VALIDATION_ERROR;
        ApiErrorResponseVAPI_1 errorResponse = new ApiErrorResponseVAPI_1(errorCode.name(), errorCode.getErrorMessage());
        logger.warn("Validation Error: message='{}'", ex.getMessage());
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    /**
     * Handles all other unhandled exceptions as a fallback.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseVAPI_1> handleAllUncaughtException(Exception ex) {
        ErrorCodeVAPI_1 errorCode = ErrorCodeVAPI_1.INTERNAL_SERVER_ERROR;
        ApiErrorResponseVAPI_1 errorResponse = new ApiErrorResponseVAPI_1(errorCode.name(), errorCode.getErrorMessage());
        logger.error("Internal Server Error: ", ex);
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }
}
```
```java
// Constants: ErrorCodeVAPI_1.java