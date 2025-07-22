package com.example.auth.initiate.api_IA_9F3E.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a successful initiate login response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InitiateLoginResponseDTO_IA_9F3E {

    private boolean success;
    private String message;
    private String transactionId;
}
```
```java