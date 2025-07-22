package com.example.repository;

import com.example.entity.User_F4B8;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository_F4B8 extends JpaRepository<User_F4B8, UUID> {

    /**
     * Finds a user by their phone number.
     * @param phoneNumber The 10-digit phone number to search for.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User_F4B8> findByPhoneNumber(String phoneNumber);
}
```
```java
src/main/java/com/example/exception/ErrorCode_F4B8.java