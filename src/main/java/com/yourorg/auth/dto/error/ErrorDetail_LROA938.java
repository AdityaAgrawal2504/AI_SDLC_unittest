package com.yourorg.auth.dto.error;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * DTO representing specific details of a validation error.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail_LROA938 {
    private String field;
    private String issue;
}
```
```java
// src/main/java/com/yourorg/auth/dto/error/ApiError_LROA938.java