package com.example.userregistration.dto;

import com.example.userregistration.model.ErrorCode_URAPI_1;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * DTO for standardized API error responses.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError_URAPI_1 {
    private ErrorCode_URAPI_1 errorCode;
    private String message;
    private Map<String, String> details;
}
```
src/main/java/com/example/userregistration/entity/User_URAPI_1.java
```java