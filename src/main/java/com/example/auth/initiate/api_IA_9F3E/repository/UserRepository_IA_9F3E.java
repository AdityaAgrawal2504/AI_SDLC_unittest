package com.example.auth.initiate.api_IA_9F3E.repository;

import com.example.auth.initiate.api_IA_9F3E.entity.User_IA_9F3E;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for User entities.
 */
@Repository
public interface UserRepository_IA_9F3E extends JpaRepository<User_IA_9F3E, String> {

    /**
     * Finds a user by their phone number.
     * @param phoneNumber The 10-digit phone number to search for.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User_IA_9F3E> findByPhoneNumber(String phoneNumber);
}
```
```java