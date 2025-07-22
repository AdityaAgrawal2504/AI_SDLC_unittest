package com.example.usersearch.repository;

import com.example.usersearch.model.UserEntity_A0B1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository_A0B1 extends JpaRepository<UserEntity_A0B1, UUID>, JpaSpecificationExecutor<UserEntity_A0B1> {
    /**
     * Finds a user by their phone number.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the user if found.
     */
    Optional<UserEntity_A0B1> findByPhoneNumber(String phoneNumber);
}
```
src/main/java/com/example/usersearch/repository/ConversationRepository_A0B1.java
```java