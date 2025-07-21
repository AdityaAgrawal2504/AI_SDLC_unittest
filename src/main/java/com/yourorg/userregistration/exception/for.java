package com.yourorg.userregistration.exception;

import com.yourorg.userregistration.enums.ErrorCodeURAPI_1201;
import org.springframework.http.HttpStatus;

/**
 * Abstract base class for custom REST exceptions.
 */
public abstract class AbstractRestExceptionURAPI_1201 extends RuntimeException {

    private final HttpStatus httpStatus;
    private final ErrorCodeURAPI_1201 errorCode;

    public AbstractRestExceptionURAPI_1201(String message, HttpStatus httpStatus, ErrorCodeURAPI_1201 errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ErrorCodeURAPI_1201 getErrorCode() {
        return errorCode;
    }
}
```
```java
// src/main/java/com/yourorg/userregistration/exception/UserAlreadyExistsExceptionURAPI_1201.java