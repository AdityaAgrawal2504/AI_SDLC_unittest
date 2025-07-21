package com.yourorg.yourapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a successful login and OTP request response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginSuccessResponseLROA9123 {

    private String status;
    private String message;
    private String otpSessionToken;

}
```
src/main/java/com/yourorg/yourapp/dto/error/ApiErrorLROA9123.java
```java