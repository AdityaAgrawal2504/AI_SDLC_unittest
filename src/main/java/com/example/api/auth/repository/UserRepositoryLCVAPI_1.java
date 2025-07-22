package com.example.api.auth.repository;

import com.example.api.auth.domain.UserLCVAPI_1;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepositoryLCVAPI_1 extends JpaRepository<UserLCVAPI_1, Long> {

    /**
     * Finds a user by their unique phone number.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the user if found.
     */
    Optional<UserLCVAPI_1> findByPhoneNumber(String phoneNumber);
}
```

src/main/java/com/example/config/SecurityConfigLCVAPI_1.java
```java