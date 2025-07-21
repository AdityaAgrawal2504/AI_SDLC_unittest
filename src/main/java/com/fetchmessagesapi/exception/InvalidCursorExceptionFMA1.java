package com.fetchmessagesapi.exception;

import com.fetchmessagesapi.enums.ErrorCodeFMA1;
import org.springframework.http.HttpStatus;

/**
 * Exception for invalid or expired cursors (400).
 */
public class InvalidCursorExceptionFMA1 extends ApiBaseExceptionFMA1 {
    public InvalidCursorExceptionFMA1(String message) {
        super(HttpStatus.BAD_REQUEST, ErrorCodeFMA1.INVALID_CURSOR, message);
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/exception/PermissionDeniedExceptionFMA1.java