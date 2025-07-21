package com.verifyotpapi.exception;

import com.verifyotpapi.util.ErrorCode_VOTP1;

/**
 * Exception thrown when a verification token is found but has expired.
 */
public class TokenExpiredException_VOTP1 extends ApiException_VOTP1 {
    public TokenExpiredException_VOTP1() {
        super(ErrorCode_VOTP1.TOKEN_EXPIRED);
    }
}
```
```java
// src/main/java/com/verifyotpapi/exception/GlobalExceptionHandler_VOTP1.java