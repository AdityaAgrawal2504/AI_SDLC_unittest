package com.example.usersearch.security;

import com.example.usersearch.model.UserEntity_A0B1;
import com.example.usersearch.repository.UserRepository_A0B1;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

/**
 * Service to load user-specific data for Spring Security.
 * It can load by phone number or by user ID.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl_A0B1 implements UserDetailsService {

    private final UserRepository_A0B1 userRepository;

    /**
     * Loads a user by their phone number. Used during the initial authentication/login process.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        UserEntity_A0B1 user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
        return buildAuthenticatedUser(user);
    }

    /**
     * Loads a user by their UUID. Used by the JWT filter after a token has been parsed.
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(UUID id) throws UsernameNotFoundException {
        UserEntity_A0B1 user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));
        return buildAuthenticatedUser(user);
    }

    private AuthenticatedUser_A0B1 buildAuthenticatedUser(UserEntity_A0B1 user) {
        return new AuthenticatedUser_A0B1(
                user.getId(),
                user.getName(),
                user.getPhoneNumber(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
```
src/main/java/com/example/usersearch/security/JwtRequestFilter_A0B1.java
```java