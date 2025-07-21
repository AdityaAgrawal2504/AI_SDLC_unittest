package com.mycompany.chatapp.validator;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Predicate;

/**
 * Validates incoming gRPC request messages based on API specification rules.
 */
@Component
public class RequestValidator_RealTimeChatStreamAPI_1122 {

    private static final Predicate<String> IS_UUID = s -> {
        try {
            UUID.fromString(s);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    };

    /**
     * Validates if a string is a valid UUID.
     * @param uuid The string to validate.
     * @return true if valid, false otherwise.
     */
    public boolean isValidUuid(String uuid) {
        return uuid != null && IS_UUID.test(uuid);
    }

    /**
     * Validates if a string is present and not blank.
     * @param value The string to validate.
     * @return true if present, false otherwise.
     */
    public boolean isRequired(String value) {
        return value != null && !value.isBlank();
    }
}
```
```java
// src/main/java/com/mycompany/chatapp/logging/KafkaAppender_RealTimeChatStreamAPI_1122.java