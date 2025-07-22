package com.example.service;

import com.example.dto.UserRegistrationRequest_UATH_1006;
import com.example.dto.UserRegistrationResponse_UATH_1007;
import com.example.entity.UserEntity_UATH_1016;
import com.example.exception.UserAlreadyExistsException_UATH_1010;
import com.example.repository.UserRepository_UATH_1017;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user management, such as registration.
 */
@Service
@RequiredArgsConstructor
public class UserService_UATH_1009 {

    private final UserRepository_UATH_1017 userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user if the phone number is not already in use.
     */
    public UserRegistrationResponse_UATH_1007 registerUser(UserRegistrationRequest_UATH_1006 request) {
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new UserAlreadyExistsException_UATH_1010("A user with the provided phone number already exists.");
        }

        String hashedPassword = passwordEncoder.encode(request.password());
        UserEntity_UATH_1016 newUser = new UserEntity_UATH_1016(request.phoneNumber(), hashedPassword);

        UserEntity_UATH_1016 savedUser = userRepository.save(newUser);

        return new UserRegistrationResponse_UATH_1007(
            savedUser.getId(),
            "User registered successfully."
        );
    }
}
```
```java
//
// Filename: src/main/java/com/example/service/OtpService_UATH_1018.java
//