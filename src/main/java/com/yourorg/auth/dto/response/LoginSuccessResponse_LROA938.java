package com.yourorg.auth.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * DTO for a successful login and OTP request response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginSuccessResponse_LROA938 {

    private String status;
    private String message;
    private String otpSessionToken;

}
```
```java
// src/main/java/com/yourorg/auth/dto/error/ErrorDetail_LROA938.java