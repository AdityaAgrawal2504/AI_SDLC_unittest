package com.example.repository;

import com.example.entity.UserEntity_UATH_1016;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for UserEntity.
 */
@Repository
public interface UserRepository_UATH_1017 extends JpaRepository<UserEntity_UATH_1016, UUID> {
    
    /**
     * Finds a user by their phone number.
     */
    Optional<UserEntity_UATH_1016> findByPhoneNumber(String phoneNumber);

    /**
     * Checks if a user exists with the given phone number.
     */
    boolean existsByPhoneNumber(String phoneNumber);
}
```
```java
//
// Filename: src/main/java/com/example/repository/ConversationRepository_CHAT_2019.java
//