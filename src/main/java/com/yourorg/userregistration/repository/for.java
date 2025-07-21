package com.yourorg.userregistration.repository;

import com.yourorg.userregistration.model.UserURAPI_1201;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for UserURAPI_1201 entity.
 * Provides standard CRUD operations and custom finders.
 */
@Repository
public interface IUserRepositoryURAPI_1201 extends JpaRepository<UserURAPI_1201, UUID> {

    /**
     * Finds a user by their phone number.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the user if found, or empty otherwise.
     */
    Optional<UserURAPI_1201> findByPhoneNumber(String phoneNumber);
}
```
```java
// src/main/java/com/yourorg/userregistration/dto/UserRegistrationRequestURAPI_1201.java