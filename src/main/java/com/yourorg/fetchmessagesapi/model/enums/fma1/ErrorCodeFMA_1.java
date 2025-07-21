package com.yourorg.fetchmessagesapi.model.enums.fma1;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enumeration of standardized error codes for the API.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCodeFMA_1 {
    INVALID_PARAMETER("INVALID_PARAMETER", HttpStatus.BAD_REQUEST),
    AUTHENTICATION_FAILED("AUTHENTICATION_FAILED", HttpStatus.UNAUTHORIZED),
    PERMISSION_DENIED("PERMISSION_DENIED", HttpStatus.FORBIDDEN),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final HttpStatus status;
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/model/dto/fma1/MessageMetadataDtoFMA_1.java