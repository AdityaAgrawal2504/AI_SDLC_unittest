package com.yourorg.fetchmessagesapi.exception.fma1;

import com.yourorg.fetchmessagesapi.model.enums.fma1.ErrorCodeFMA_1;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base custom exception for the application.
 */
@Getter
public class CustomApiExceptionFMA_1 extends RuntimeException {
    private final ErrorCodeFMA_1 errorCode;
    private final HttpStatus httpStatus;

    public CustomApiExceptionFMA_1(ErrorCodeFMA_1 errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
    }
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/exception/fma1/AuthenticationExceptionFMA_1.java