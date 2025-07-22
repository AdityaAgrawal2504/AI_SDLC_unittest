package com.example.service.impl;

import com.example.dto.UserRegistrationRequest_F4B8;
import com.example.dto.UserRegistrationResponse_F4B8;
import com.example.entity.User_F4B8;
import com.example.exception.PasswordHashingException_F4B8;
import com.example.exception.UserAlreadyExistsException_F4B8;
import com.example.repository.UserRepository_F4B8;
import com.example.service.IUserRegistrationService_F4B8;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added for transactional integrity

/**
 * Implementation of the user registration service.
 */
@Service
public class UserRegistrationServiceImpl_F4B8 implements IUserRegistrationService_F4B8 {

    private final UserRepository_F4B8 userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationServiceImpl_F4B8(UserRepository_F4B8 userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user after validating the request and ensuring the user does not already exist.
     * @param request The user registration request containing phone number and password.
     * @return A response DTO with the new user's ID.
     */
    @Override
    @Transactional // Ensures the operation is atomic
    public UserRegistrationResponse_F4B8 registerUser(UserRegistrationRequest_F4B8 request) {
        userRepository.findByPhoneNumber(request.getPhoneNumber()).ifPresent(user -> {
            throw new UserAlreadyExistsException_F4B8("A user with the phone number " + request.getPhoneNumber() + " already exists.");
        });

        User_F4B8 newUser = new User_F4B8();
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setPasswordHash(hashPassword(request.getPassword()));
        
        User_F4B8 savedUser = userRepository.save(newUser);
        
        return new UserRegistrationResponse_F4B8(
                savedUser.getId().toString(),
                "User registered successfully."
        );
    }

    /**
     * Hashes the user's plain-text password using the configured PasswordEncoder.
     * @param plainTextPassword The password to hash.
     * @return The securely hashed password.
     * @throws PasswordHashingException_F4B8 if the hashing algorithm fails.
     */
    private String hashPassword(String plainTextPassword) {
        try {
            return passwordEncoder.encode(plainTextPassword);
        } catch (Exception e) {
            throw new PasswordHashingException_F4B8("Failed to hash user password.", e);
        }
    }
}
```
```java
src/main/java/com/example/controller/UserRegistrationController_F4B8.java