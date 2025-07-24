package com.example.messagingapp.security;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.UUID;

@Getter
public class AuthenticatedUser extends User {

    private final UUID id;

    /**
     * Constructor for the authenticated user principal.
     * @param userId The UUID of the user.
     * @param username The username (phone number).
     */
    public AuthenticatedUser(UUID userId, String username) {
        super(username, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        this.id = userId;
    }
}