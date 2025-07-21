package com.mycompany.chatapp.util;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages idempotency keys to prevent duplicate processing.
 * This is a simple in-memory implementation. For production, a distributed cache
 * like Redis or an atomic database operation would be required.
 */
@Component
public class IdempotencyManager_RealTimeChatStreamAPI_1122 {

    private final Set<String> processedKeys = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Attempts to claim an idempotency key.
     * @param key The idempotency key from the client request.
     * @return true if the key is new and was successfully claimed, false if it was a duplicate.
     */
    public boolean claim(String key) {
        return processedKeys.add(key);
    }
}
```
```java
// src/main/java/com/mycompany/chatapp/util/SanitizationUtil_RealTimeChatStreamAPI_1122.java