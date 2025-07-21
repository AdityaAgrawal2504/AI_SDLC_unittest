package com.fetchconversations.api.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines the fields by which conversations can be sorted.
 */
public enum SortFieldFCA911 {
    LAST_ACTIVITY("lastActivity", "updatedAt"),
    SEEN("seen", "lastMessageSeen"); // 'lastMessageSeen' is a hypothetical entity field

    private final String value;
    private final String dbField;

    SortFieldFCA911(String value, String dbField) {
        this.value = value;
        this.dbField = dbField;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public String getDbField() {
        return dbField;
    }

    public static SortFieldFCA911 fromValue(String value) {
        for (SortFieldFCA911 field : values()) {
            if (field.value.equalsIgnoreCase(value)) {
                return field;
            }
        }
        throw new IllegalArgumentException("Invalid sort field: " + value);
    }
}
```
```java
// src/main/java/com/fetchconversations/api/enums/SortOrderFCA911.java