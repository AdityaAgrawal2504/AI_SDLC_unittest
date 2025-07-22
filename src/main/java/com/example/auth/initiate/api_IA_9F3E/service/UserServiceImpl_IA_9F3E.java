package com.example.auth.initiate.api_IA_9F3E.service;

import com.example.auth.initiate.api_IA_9F3E.entity.User_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.repository.UserRepository_IA_9F3E;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the User service for data access operations.
 */
@Service
public class UserServiceImpl_IA_9F3E implements IUserService_IA_9F3E {

    private final UserRepository_IA_9F3E userRepository;

    public UserServiceImpl_IA_9F3E(UserRepository_IA_9F3E userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a user by their phone number using the repository.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the User if found, otherwise an empty Optional.
     */
    @Override
    public Optional<User_IA_9F3E> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
}
```
```java