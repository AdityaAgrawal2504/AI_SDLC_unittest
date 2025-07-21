package com.yourorg.auth.repository;

import com.yourorg.auth.model.User_LROA938;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository_LROA938 extends JpaRepository<User_LROA938, Long> {

    /**
     * Finds a user by their phone number.
     * @param phone The user's phone number.
     * @return An Optional containing the user if found.
     */
    Optional<User_LROA938> findByPhone(String phone);
}
```
```java
// src/main/java/com/yourorg/auth/config/SecurityConfig_LROA938.java