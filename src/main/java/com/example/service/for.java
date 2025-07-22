package com.example.service;

import lombok.Getter;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A data holder class for OTP information, including attempts.
 */
@Getter
class OtpData_UATH_1019 {
    private final String otp;
    private final Instant timestamp;
    private final AtomicInteger attempts;

    OtpData_UATH_1019(String otp, Instant timestamp) {
        this.otp = otp;
        this.timestamp = timestamp;
        this.attempts = new AtomicInteger(0);
    }
    
    public int incrementAttempts() {
        return this.attempts.incrementAndGet();
    }
}
```
```java
//
// Filename: src/main/java/com/example/service/ConversationService_CHAT_2005.java
//