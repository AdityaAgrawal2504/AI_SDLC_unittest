package com.yourorg.yourapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA Entity representing a user in the system.
 */
@Entity
@Table(name = "app_user_lroa9123", indexes = {
    @Index(name = "idx_user_phone", columnList = "phone", unique = true)
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLROA9123 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private boolean accountLocked = false;
    
    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;
}
```
src/main/java/com/yourorg/yourapp/repository/UserRepositoryLROA9123.java
```java