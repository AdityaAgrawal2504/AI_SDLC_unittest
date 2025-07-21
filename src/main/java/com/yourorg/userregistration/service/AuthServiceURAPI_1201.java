package com.yourorg.userregistration.service;

import com.yourorg.userregistration.dto.UserRegistrationRequestURAPI_1201;
import com.yourorg.userregistration.dto.UserRegistrationResponseURAPI_1201;
import com.yourorg.userregistration.exception.UserAlreadyExistsExceptionURAPI_1201;
import com.yourorg.userregistration.model.UserURAPI_1201;
import com.yourorg.userregistration.repository.IUserRepositoryURAPI_1201;
import com.yourorg.userregistration.security.IPasswordHasherURAPI_1201;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for user registration logic.
 */
@Service
public class AuthServiceURAPI_1201 implements IAuthServiceURAPI_1201 {

    private final IUserRepositoryURAPI_1201 userRepository;
    private final IPasswordHasherURAPI_1201 passwordHasher;

    public AuthServiceURAPI_1201(IUserRepositoryURAPI_1201 userRepository, IPasswordHasherURAPI_1201 passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    /**
     * Handles the business logic for registering a new user.
     * @param request The registration request details.
     * @return A response with the new user's ID and status.
     * @throws UserAlreadyExistsExceptionURAPI_1201 if the phone number is already registered.
     */
    @Override
    @Transactional
    public UserRegistrationResponseURAPI_1201 register(UserRegistrationRequestURAPI_1201 request) {
        userRepository.findByPhoneNumber(request.getPhoneNumber()).ifPresent(user -> {
            throw new UserAlreadyExistsExceptionURAPI_1201(request.getPhoneNumber());
        });

        String hashedPassword = passwordHasher.hash(request.getPassword());

        UserURAPI_1201 newUser = new UserURAPI_1201();
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setPasswordHash(hashedPassword);

        UserURAPI_1201 savedUser = userRepository.save(newUser);

        return new UserRegistrationResponseURAPI_1201(savedUser.getId(), "REGISTRATION_SUCCESSFUL");
    }
}
```
```java
// src/main/java/com/yourorg/userregistration/aop/LoggingAspectURAPI_1201.java