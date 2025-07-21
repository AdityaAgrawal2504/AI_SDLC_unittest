package com.yourorg.fetchconversationsapi.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration for the sorting order (ascending or descending).
 */
public enum SortOrderFCA8123 {
    ASC("asc"),
    DESC("desc");

    private final String value;

    SortOrderFCA8123(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
```
src/main/java/com/yourorg/fetchconversationsapi/dto/ApiErrorFCA8123.java
```java