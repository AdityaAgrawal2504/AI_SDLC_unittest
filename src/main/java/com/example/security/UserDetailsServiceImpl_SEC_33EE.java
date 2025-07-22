package com.example.security;

import com.example.entity.UserEntity_UATH_1016;
import com.example.repository.UserRepository_UATH_1017;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * Service to load user-specific data for Spring Security.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl_SEC_33EE implements UserDetailsService {

    private final UserRepository_UATH_1017 userRepository;

    /**
     * Loads a user by their username (in this case, user ID).
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserEntity_UATH_1016 userEntity = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        return new User(userEntity.getId().toString(), userEntity.getPassword(), Collections.emptyList());
    }
}
```
```java
//
// Filename: src/main/java/com/example/service/AuthService_UATH_1008.java
//