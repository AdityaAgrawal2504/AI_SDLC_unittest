package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * Data Transfer Object for standardized API error responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError_F4B8 {

    private String errorCode;
    private String message;
    private Map<String, String> details;

    public ApiError_F4B8(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
```
```java
src/main/java/com/example/entity/User_F4B8.java