package com.example.userregistration.service;

import com.example.userregistration.dto.UserRegistrationRequest_URAPI_1;
import com.example.userregistration.dto.UserRegistrationResponse_URAPI_1;
import com.example.userregistration.entity.User_URAPI_1;
import com.example.userregistration.exception.PasswordHashingException_URAPI_1;
import com.example.userregistration.exception.UserAlreadyExistsException_URAPI_1;
import com.example.userregistration.repository.UserRepository_URAPI_1;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for user registration.
 *
 * <!--
 * mermaid
 * graph TD
 *     A[Controller] --> B(UserRegistrationService_URAPI_1);
 *     B --> C{PasswordEncoder};
 *     B --> D{UserRepository_URAPI_1};
 * -->
 */
@Service
@RequiredArgsConstructor
public class UserRegistrationService_URAPI_1 implements IUserRegistrationService_URAPI_1 {

    private final UserRepository_URAPI_1 userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user based on the provided request details.
     * This method is transactional, ensuring that all database operations are atomic.
     * @param request DTO containing the phone number and password.
     * @return DTO with the newly created user's ID and a success message.
     */
    @Override
    @Transactional
    public UserRegistrationResponse_URAPI_1 registerUser(UserRegistrationRequest_URAPI_1 request) {
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new UserAlreadyExistsException_URAPI_1(
                "A user with phone number " + request.getPhoneNumber() + " already exists."
            );
        }

        String hashedPassword = hashPassword(request.getPassword());

        User_URAPI_1 newUser = User_URAPI_1.builder()
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(hashedPassword)
                .build();

        User_URAPI_1 savedUser = userRepository.save(newUser);

        return UserRegistrationResponse_URAPI_1.builder()
                .userId(savedUser.getId().toString())
                .message("User registered successfully.")
                .build();
    }

    /**
     * Hashes the provided plain-text password.
     * @param plainTextPassword The password to hash.
     * @return The securely hashed password.
     * @throws PasswordHashingException_URAPI_1 if an unexpected error occurs during hashing.
     */
    private String hashPassword(String plainTextPassword) {
        try {
            return passwordEncoder.encode(plainTextPassword);
        } catch (Exception ex) {
            throw new PasswordHashingException_URAPI_1("Failed to hash user password.", ex);
        }
    }
}
```
src/main/java/com/example/userregistration/controller/UserRegistrationController_URAPI_1.java
```java