package com.example.userregistration.repository;

import com.example.userregistration.entity.User_URAPI_1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository_URAPI_1 extends JpaRepository<User_URAPI_1, UUID> {

    /**
     * Checks if a user exists with the given phone number.
     * @param phoneNumber The phone number to check.
     * @return true if a user exists, false otherwise.
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Finds a user by their phone number.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the user if found.
     */
    Optional<User_URAPI_1> findByPhoneNumber(String phoneNumber);
}
```
src/main/java/com/example/userregistration/exception/UserAlreadyExistsException_URAPI_1.java
```java