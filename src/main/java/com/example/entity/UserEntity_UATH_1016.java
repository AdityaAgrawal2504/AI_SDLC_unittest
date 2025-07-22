package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserEntity_UATH_1016 {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false, length = 10)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column
    private String displayName;

    @Column
    private String avatarUrl;

    public UserEntity_UATH_1016(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.displayName = "User " + phoneNumber.substring(6); // Default display name
        this.avatarUrl = "https://i.pravatar.cc/150?u=" + phoneNumber; // Default avatar
    }
}
```
```java
//
// Filename: src/main/java/com/example/entity/ConversationEntity_CHAT_2017.java
//