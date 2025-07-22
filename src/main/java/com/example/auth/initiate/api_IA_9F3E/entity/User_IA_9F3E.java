package com.example.auth.initiate.api_IA_9F3E.entity;

import com.example.auth.initiate.api_IA_9F3E.constants.UserStatus_IA_9F3E;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user entity in the data persistence layer.
 */
@Entity
@Table(name = "app_user_ia_9f3e")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User_IA_9F3E {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus_IA_9F3E status;
}
```
```java