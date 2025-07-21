package com.verifyotpapi.exception;

import com.verifyotpapi.util.ErrorCode_VOTP1;

/**
 * Exception thrown when the maximum number of OTP verification attempts is exceeded.
 */
public class MaxAttemptsExceededException_VOTP1 extends ApiException_VOTP1 {
    public MaxAttemptsExceededException_VOTP1() {
        super(ErrorCode_VOTP1.MAX_ATTEMPTS_REACHED);
    }
}
```
```java
// src/main/java/com/verifyotpapi/exception/ResourceNotFoundException_VOTP1.java