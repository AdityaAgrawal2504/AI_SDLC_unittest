package com.yourorg.verifyotp.service;

import com.yourorg.verifyotp.dto.VerifyOtpSuccessResponseVAPI_1;

public interface ITokenServiceVAPI_1 {

    /**
     * Generates session tokens (access and refresh) for a given user ID.
     */
    VerifyOtpSuccessResponseVAPI_1 generateSessionTokens(String userId);
}
```
```java
// Service Implementation: TokenServiceVAPI_1.java