package com.example.auth.initiate.api_IA_9F3E.service;

import com.example.auth.initiate.api_IA_9F3E.entity.User_IA_9F3E;

import java.util.Optional;

/**
 * Service contract for user data retrieval and management.
 */
public interface IUserService_IA_9F3E {

    /**
     * Retrieves a user entity from the data store by their phone number.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the User if found, otherwise an empty Optional.
     */
    Optional<User_IA_9F3E> findByPhoneNumber(String phoneNumber);
}
```
```java