package com.fetchmessagesapi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration for standardized error codes.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCodeFMA1 {
    INVALID_PARAMETER("INVALID_PARAMETER"),
    AUTHENTICATION_FAILED("AUTHENTICATION_FAILED"),
    PERMISSION_DENIED("PERMISSION_DENIED"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    INVALID_CURSOR("INVALID_CURSOR");

    private final String code;
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/dto/MessageContentDtoFMA1.java