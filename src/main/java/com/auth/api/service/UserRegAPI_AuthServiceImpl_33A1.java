package com.auth.api.service;

import com.auth.api.dto.UserRegAPI_UserRegistrationRequest_33A1;
import com.auth.api.dto.UserRegAPI_UserRegistrationResponse_33A1;
import com.auth.api.enums.UserRegAPI_RegistrationStatus_33A1;
import com.auth.api.exception.UserRegAPI_ConflictException_33A1;
import com.auth.api.logging.UserRegAPI_Loggable_33A1;
import com.auth.api.model.UserRegAPI_UserEntity_33A1;
import com.auth.api.repository.UserRegAPI_UserRepository_33A1;
import com.auth.api.service.hasher.UserRegAPI_PasswordHasher_33A1;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements the user registration business logic.
 */
@Service
@RequiredArgsConstructor
public class UserRegAPI_AuthServiceImpl_33A1 implements UserRegAPI_AuthService_33A1 {

    private static final Logger logger = LogManager.getLogger(UserRegAPI_AuthServiceImpl_33A1.class);

    private final UserRegAPI_UserRepository_33A1 userRepository;
    private final UserRegAPI_PasswordHasher_33A1 passwordHasher;

    /**
     * Registers a new user, handling validation, password hashing, and persistence.
     * @param request The user registration request DTO.
     * @return A DTO with the new user's ID and status.
     * @throws UserRegAPI_ConflictException_33A1 if a user with the phone number already exists.
     */
    @Override
    @Transactional
    @UserRegAPI_Loggable_33A1
    public UserRegAPI_UserRegistrationResponse_33A1 register(UserRegAPI_UserRegistrationRequest_33A1 request) {
        logger.info("Attempting to register user with phone number: {}", request.getPhoneNumber());

        userRepository.findByPhoneNumber(request.getPhoneNumber()).ifPresent(user -> {
            logger.warn("Registration failed: Phone number {} already exists.", request.getPhoneNumber());
            throw new UserRegAPI_ConflictException_33A1("A user with this phone number already exists.");
        });

        String hashedPassword = passwordHasher.hash(request.getPassword());

        UserRegAPI_UserEntity_33A1 newUser = new UserRegAPI_UserEntity_33A1();
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setPasswordHash(hashedPassword);

        UserRegAPI_UserEntity_33A1 savedUser = userRepository.save(newUser);
        logger.info("Successfully registered user with ID: {}", savedUser.getId());

        return new UserRegAPI_UserRegistrationResponse_33A1(
                savedUser.getId(),
                UserRegAPI_RegistrationStatus_33A1.REGISTRATION_SUCCESSFUL
        );
    }
}
```
```java
// src/main/java/com/auth/api/config/UserRegAPI_SecurityConfig_33A1.java