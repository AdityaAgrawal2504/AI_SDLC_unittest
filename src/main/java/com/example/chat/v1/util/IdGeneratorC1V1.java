package com.example.chat.v1.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Utility for generating unique, prefixed identifiers.
 */
@Component
public class IdGeneratorC1V1 {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ID_LENGTH = 24;

    /**
     * Generates a new random ID with a given prefix.
     */
    public String newId(String prefix) {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return prefix + "_" + sb.toString();
    }

    /**
     * Generates a new message ID.
     */
    public String newMessageId() {
        return newId("msg");
    }

    /**
     * Generates a new conversation ID.
     */
    public String newConversationId() {
        return newId("conv");
    }

    /**
     * Generates a new user ID.
     */
    public String newUserId() {
        return newId("user");
    }
}
```
```java
// src/main/java/com/example/chat/v1/util/ProtoMapperC1V1.java