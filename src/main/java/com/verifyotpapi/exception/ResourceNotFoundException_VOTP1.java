package com.verifyotpapi.exception;

import com.verifyotpapi.util.ErrorCode_VOTP1;

/**
 * Exception thrown when a verification token is not found.
 */
public class ResourceNotFoundException_VOTP1 extends ApiException_VOTP1 {
    public ResourceNotFoundException_VOTP1() {
        super(ErrorCode_VOTP1.TOKEN_NOT_FOUND);
    }
}
```
```java
// src/main/java/com/verifyotpapi/exception/TokenExpiredException_VOTP1.java