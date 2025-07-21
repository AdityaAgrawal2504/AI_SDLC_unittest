package com.yourorg.auth.exception;

import com.yourorg.auth.enums.ErrorCode_LROA938;
import lombok.Getter;

/**
 * Custom runtime exception used for handling all application-specific errors.
 */
@Getter
public class ApplicationException_LROA938 extends RuntimeException {

    private final ErrorCode_LROA938 errorCode;

    public ApplicationException_LROA938(ErrorCode_LROA938 errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApplicationException_LROA938(ErrorCode_LROA938 errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
```
```java
// src/main/java/com/yourorg/auth/exception/GlobalExceptionHandler_LROA938.java