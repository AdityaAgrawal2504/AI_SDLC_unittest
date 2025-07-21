package com.verifyotpapi.exception;

import com.verifyotpapi.util.ErrorCode_VOTP1;

/**
 * Exception thrown when the provided OTP code is incorrect.
 */
public class InvalidOtpException_VOTP1 extends ApiException_VOTP1 {
    public InvalidOtpException_VOTP1() {
        super(ErrorCode_VOTP1.INVALID_OTP);
    }
}
```
```java
// src/main/java/com/verifyotpapi/exception/MaxAttemptsExceededException_VOTP1.java