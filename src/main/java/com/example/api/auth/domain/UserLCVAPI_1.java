package com.example.api.auth.domain;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * JPA Entity representing a user in the database.
 */
@Entity
@Table(name = "users_lcvapi")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLCVAPI_1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatusLCVAPI_1 status;

    @Column(nullable = false)
    private int failedLoginAttempts;
}
```

src/main/java/com/example/api/auth/domain/UserStatusLCVAPI_1.java
```java