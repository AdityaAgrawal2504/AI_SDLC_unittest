package com.example.usersearch.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode_A0B1 {
    VALIDATION_QUERY_MISSING("VALIDATION_QUERY_MISSING", "The 'q' query parameter is required."),
    VALIDATION_QUERY_TOO_SHORT("VALIDATION_QUERY_TOO_SHORT", "Search query must be at least 3 characters long."),
    VALIDATION_QUERY_TOO_LONG("VALIDATION_QUERY_TOO_LONG", "Search query cannot be more than 100 characters long."),
    VALIDATION_INVALID_PAGE_NUMBER("VALIDATION_INVALID_PAGE_NUMBER", "Page number must be a positive integer."),
    VALIDATION_INVALID_PAGE_SIZE("VALIDATION_INVALID_PAGE_SIZE", "Page size must be an integer between 1 and 100."),

    AUTH_TOKEN_MISSING("AUTH_TOKEN_MISSING", "Authentication token is missing from the Authorization header."),
    AUTH_TOKEN_INVALID("AUTH_TOKEN_INVALID", "Authentication token is invalid or expired."),

    DATABASE_SEARCH_FAILED("DATABASE_SEARCH_FAILED", "An error occurred while searching for users in the database."),
    UNEXPECTED_SERVER_ERROR("UNEXPECTED_SERVER_ERROR", "An unexpected internal server error occurred.");

    private final String code;
    private final String message;
}
```
src/main/java/com/example/usersearch/exception/DatabaseSearchException_A0B1.java
```java