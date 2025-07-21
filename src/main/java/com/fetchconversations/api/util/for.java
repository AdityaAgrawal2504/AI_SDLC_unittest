package com.fetchconversations.api.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Utility class for authentication and authorization related tasks.
 */
@Component
public class AuthUtilFCA911 {

    /**
     * Retrieves the authenticated user's ID from the security context.
     * In a real application, this would parse a custom Principal or JWT claims.
     * For this example, it assumes the username is a UUID.
     * @return The UUID of the currently authenticated user.
     * @throws IllegalStateException if no user is authenticated.
     */
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            // This case would be handled by Spring Security, leading to a 401.
            // This check is for defensive programming within the service layer.
            throw new IllegalStateException("User not authenticated.");
        }
        // In a real app, you might cast to a custom UserDetails object.
        String username = ((User) authentication.getPrincipal()).getUsername();
        try {
            return UUID.fromString(username);
        } catch (IllegalArgumentException ex) {
            // Handle cases where the username is not a valid UUID
            throw new IllegalStateException("Authenticated principal's name is not a valid UUID.");
        }
    }
}
```
```java
// src/main/java/com/fetchconversations/api/service/ConversationServiceFCA911.java