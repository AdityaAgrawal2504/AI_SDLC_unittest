package com.yourorg.auth.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for standardized API error responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError_LROA938 {

    private String errorCode;
    private String errorMessage;
    private List<ErrorDetail_LROA938> errorDetails;
}
```
```java
// src/main/java/com/yourorg/auth/enums/ErrorCode_LROA938.java