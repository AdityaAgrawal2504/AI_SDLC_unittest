package com.fetchmessagesapi.exception;

import com.fetchmessagesapi.enums.ErrorCodeFMA1;
import org.springframework.http.HttpStatus;

/**
 * Exception for resources that cannot be found (404).
 */
public class ResourceNotFoundExceptionFMA1 extends ApiBaseExceptionFMA1 {
    public ResourceNotFoundExceptionFMA1(String message) {
        super(HttpStatus.NOT_FOUND, ErrorCodeFMA1.RESOURCE_NOT_FOUND, message);
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/exception/GlobalExceptionHandlerFMA1.java