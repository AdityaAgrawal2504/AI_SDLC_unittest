package com.verifyotpapi.exception;

import com.verifyotpapi.util.ErrorCode_VOTP1;
import lombok.Getter;

/**
 * Base class for all custom API-related exceptions.
 */
@Getter
public abstract class ApiException_VOTP1 extends RuntimeException {
    private final ErrorCode_VOTP1 errorCode;

    public ApiException_VOTP1(ErrorCode_VOTP1 errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
```
```java
// src/main/java/com/verifyotpapi/exception/InvalidOtpException_VOTP1.java