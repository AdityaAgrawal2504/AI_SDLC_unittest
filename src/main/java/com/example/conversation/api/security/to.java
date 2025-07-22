package com.example.conversation.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Utility class to provide access to security-related context, such as the current user's ID.
 */
@Component
public class SecurityUtilFCA1 {

    /**
     * Retrieves the authenticated user's ID.
     * In a real application, this would parse a JWT or session to get the user principal.
     * For this implementation, it returns a hardcoded UUID to simulate an authenticated user.
     *
     * @return The UUID of the currently authenticated user.
     */
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // In a real scenario, you would extract the user ID from the principal.
        // For example:
        // if (authentication != null && authentication.getPrincipal() instanceof MyUserDetails) {
        //     return ((MyUserDetails) authentication.getPrincipal()).getId();
        // }
        // For now, we return a fixed ID for demonstration purposes.
        // This fixed ID also matches CURRENT_USER_ID in ConversationServiceImplFCA1 for consistency.
        return UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    }
}
src/main/java/com.example/conversation/api/service/ConversationServiceFCA1.java