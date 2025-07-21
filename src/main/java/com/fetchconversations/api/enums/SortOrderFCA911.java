package com.fetchconversations.api.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines the order for sorting (ascending or descending).
 */
public enum SortOrderFCA911 {
    ASC("asc"),
    DESC("desc");

    private final String value;

    SortOrderFCA911(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
```
```java
// src/main/java/com/fetchconversations/api/dto/ApiErrorFCA911.java