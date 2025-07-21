package com.verifyotpapi.service;

import com.verifyotpapi.dto.response.VerifyOtpSuccessResponse_VOTP1;

/**
 * Interface for generating and validating tokens.
 */
public interface ITokenService_VOTP1 {
    /**
     * Generates session tokens (access and refresh) for a given user ID.
     */
    VerifyOtpSuccessResponse_VOTP1 generateSessionTokens(String userId);
}
```
```java
// src/main/java/com/verifyotpapi/service/TokenService_VOTP1.java