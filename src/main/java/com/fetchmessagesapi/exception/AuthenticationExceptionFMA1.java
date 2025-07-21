package com.fetchmessagesapi.exception;

import com.fetchmessagesapi.enums.ErrorCodeFMA1;
import org.springframework.http.HttpStatus;

/**
 * Exception for authentication failures (401).
 */
public class AuthenticationExceptionFMA1 extends ApiBaseExceptionFMA1 {
    public AuthenticationExceptionFMA1(String message) {
        super(HttpStatus.UNAUTHORIZED, ErrorCodeFMA1.AUTHENTICATION_FAILED, message);
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/exception/InvalidCursorExceptionFMA1.java