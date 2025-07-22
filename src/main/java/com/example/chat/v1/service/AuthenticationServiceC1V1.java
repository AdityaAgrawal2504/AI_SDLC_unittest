package com.example.chat.v1.service;

import com.example.chat.v1.logging.LoggableC1V1;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Mock authentication service to validate JWTs.
 */
@Service
public class AuthenticationServiceC1V1 {

    private final MockDataRepositoryC1V1 dataRepository;

    public AuthenticationServiceC1V1(MockDataRepositoryC1V1 dataRepository) {
        this.dataRepository = dataRepository;
    }

    /**
     * Validates a JWT and returns the user ID. In a real app, this would involve a JWT library.
     */
    @LoggableC1V1
    public Optional<String> getUserIdFromToken(String token) {
        // This is a mock implementation.
        // A real implementation would parse and verify the JWT signature.
        // For this mock, we assume the token is the user ID directly.
        // For testing purposes, valid tokens map directly to existing user IDs.
        // e.g., token "user_abc123" maps to user with ID "user_abc123" if it exists.
        return dataRepository.findUserById(token).map(user -> user.getId());
    }
}
```
```java
// src/main/java/com/example/chat/v1/service/UserStreamManagerC1V1.java