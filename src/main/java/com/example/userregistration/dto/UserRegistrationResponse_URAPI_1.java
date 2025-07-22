package com.example.userregistration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a successful user registration response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationResponse_URAPI_1 {
    private String userId;
    private String message;
}
```
src/main/java/com/example/userregistration/dto/ApiError_URAPI_1.java
```java