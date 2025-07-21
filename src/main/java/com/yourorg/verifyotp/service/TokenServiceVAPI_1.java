package com.yourorg.verifyotp.service;

import com.yourorg.verifyotp.dto.VerifyOtpSuccessResponseVAPI_1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenServiceVAPI_1 implements ITokenServiceVAPI_1 {

    @Value("${jwt.access.token.expiry:3600}")
    private long accessTokenExpiry;
    
    /**
     * Generates mock session tokens for a given user ID.
     * In a real application, this would use a JWT library like jjwt to create signed tokens.
     */
    @Override
    public VerifyOtpSuccessResponseVAPI_1 generateSessionTokens(String userId) {
        // This is a mock implementation.
        String accessToken = "mock-access-token-" + userId + "-" + UUID.randomUUID();
        String refreshToken = "mock-refresh-token-" + userId + "-" + UUID.randomUUID();

        return new VerifyOtpSuccessResponseVAPI_1(accessToken, accessTokenExpiry, refreshToken);
    }
}
```
```java
// Service Interface: IAuthServiceVAPI_1.java