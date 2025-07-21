package com.yourorg.fetchmessagesapi.exception.fma1;

import com.yourorg.fetchmessagesapi.model.enums.fma1.ErrorCodeFMA_1;

/**
 * Exception for authorization failures.
 */
public class PermissionDeniedExceptionFMA_1 extends CustomApiExceptionFMA_1 {
    public PermissionDeniedExceptionFMA_1(String message) {
        super(ErrorCodeFMA_1.PERMISSION_DENIED, message);
    }
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/exception/fma1/ResourceNotFoundExceptionFMA_1.java