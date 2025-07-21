package com.yourorg.userregistration.dto;

import java.util.UUID;

/**
 * DTO for the successful user registration response.
 */
public class UserRegistrationResponseURAPI_1201 {

    private UUID userId;
    private String status;

    public UserRegistrationResponseURAPI_1201(UUID userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
```
```java
// src/main/java/com/yourorg/userregistration/dto/ApiErrorURAPI_1201.java