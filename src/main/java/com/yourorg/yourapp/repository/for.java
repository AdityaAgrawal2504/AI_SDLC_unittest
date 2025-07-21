package com.yourorg.yourapp.repository;

import com.yourorg.yourapp.model.UserLROA9123;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing User data from the database.
 */
@Repository
public interface UserRepositoryLROA9123 extends JpaRepository<UserLROA9123, Long> {

    /**
     * Finds a user by their phone number.
     * @param phone The user's phone number in E.164 format.
     * @return An Optional containing the user if found.
     */
    Optional<UserLROA9123> findByPhone(String phone);
}
```
src/main/java/com/yourorg/yourapp/exception/ApiExceptionLROA9123.java
```java