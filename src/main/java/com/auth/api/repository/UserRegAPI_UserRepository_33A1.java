package com.auth.api.repository;

import com.auth.api.model.UserRegAPI_UserEntity_33A1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRegAPI_UserRepository_33A1 extends JpaRepository<UserRegAPI_UserEntity_33A1, UUID> {

    /**
     * Finds a user by their phone number.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the user if found.
     */
    Optional<UserRegAPI_UserEntity_33A1> findByPhoneNumber(String phoneNumber);
}
```
```java
// src/main/java/com/auth/api/exception/UserRegAPI_ConflictException_33A1.java