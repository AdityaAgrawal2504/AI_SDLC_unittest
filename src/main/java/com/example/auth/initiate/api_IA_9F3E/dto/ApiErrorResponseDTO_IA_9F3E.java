package com.example.auth.initiate.api_IA_9F3E.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.example.auth.initiate.api_IA_9F3E.constants.ErrorCode_IA_9F3E;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standardized DTO for API error responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponseDTO_IA_9F3E {

    private boolean success;
    private ErrorCode_IA_9F3E errorCode;
    private String message;
    private Object details;
}
```
```java