package com.yourorg.fetchmessagesapi.exception.fma1;

import com.yourorg.fetchmessagesapi.model.enums.fma1.ErrorCodeFMA_1;

/**
 * Exception for when a requested resource is not found.
 */
public class ResourceNotFoundExceptionFMA_1 extends CustomApiExceptionFMA_1 {
    public ResourceNotFoundExceptionFMA_1(String message) {
        super(ErrorCodeFMA_1.RESOURCE_NOT_FOUND, message);
    }
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/exception/fma1/GlobalExceptionHandlerFMA_1.java