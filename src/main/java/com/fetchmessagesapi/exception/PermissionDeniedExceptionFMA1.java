package com.fetchmessagesapi.exception;

import com.fetchmessagesapi.enums.ErrorCodeFMA1;
import org.springframework.http.HttpStatus;

/**
 * Exception for authorization failures (403).
 */
public class PermissionDeniedExceptionFMA1 extends ApiBaseExceptionFMA1 {
    public PermissionDeniedExceptionFMA1(String message) {
        super(HttpStatus.FORBIDDEN, ErrorCodeFMA1.PERMISSION_DENIED, message);
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/exception/ResourceNotFoundExceptionFMA1.java