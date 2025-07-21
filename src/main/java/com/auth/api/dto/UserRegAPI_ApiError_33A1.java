package com.auth.api.dto;

import com.auth.api.enums.UserRegAPI_ErrorCode_33A1;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Data Transfer Object for API error responses.
 */
@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegAPI_ApiError_33A1 {

    private final UserRegAPI_ErrorCode_33A1 errorCode;
    private final String message;
    private Map<String, String> details;
}
```
```java
// src/main/java/com/auth/api/model/UserRegAPI_UserEntity_33A1.java