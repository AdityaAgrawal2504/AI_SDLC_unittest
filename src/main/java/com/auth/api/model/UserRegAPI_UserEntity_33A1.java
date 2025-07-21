package com.auth.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents the User entity in the database.
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_phone_number", columnList = "phoneNumber", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class UserRegAPI_UserEntity_33A1 {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String passwordHash;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
```
```java
// src/main/java/com/auth/api/repository/UserRegAPI_UserRepository_33A1.java