package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for a successful user registration response.
 * Contains the unique ID of the new user and a confirmation message.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationResponse_F4B8 {

    private String userId;
    private String message;
}
```
```java
src/main/java/com/example/dto/ApiError_F4B8.java