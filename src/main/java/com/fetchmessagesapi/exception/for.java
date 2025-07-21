package com.fetchmessagesapi.exception;

import com.fetchmessagesapi.enums.ErrorCodeFMA1;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base class for custom API exceptions.
 */
@Getter
public abstract class ApiBaseExceptionFMA1 extends RuntimeException {
    private final HttpStatus status;
    private final ErrorCodeFMA1 errorCode;

    protected ApiBaseExceptionFMA1(HttpStatus status, ErrorCodeFMA1 errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/exception/AuthenticationExceptionFMA1.java