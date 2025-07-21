package com.auth.api.dto;

import com.auth.api.enums.UserRegAPI_RegistrationStatus_33A1;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data Transfer Object for a successful user registration response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegAPI_UserRegistrationResponse_33A1 {

    private UUID userId;
    private UserRegAPI_RegistrationStatus_33A1 status;
}
```
```java
// src/main/java/com/auth/api/dto/UserRegAPI_ApiError_33A1.java