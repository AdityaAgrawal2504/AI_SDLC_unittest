package com.yourorg.fetchmessagesapi.exception.fma1;

import com.yourorg.fetchmessagesapi.model.enums.fma1.ErrorCodeFMA_1;

/**
 * Exception for invalid request parameters.
 */
public class InvalidParameterExceptionFMA_1 extends CustomApiExceptionFMA_1 {
    public InvalidParameterExceptionFMA_1(String message) {
        super(ErrorCodeFMA_1.INVALID_PARAMETER, message);
    }
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/exception/fma1/PermissionDeniedExceptionFMA_1.java