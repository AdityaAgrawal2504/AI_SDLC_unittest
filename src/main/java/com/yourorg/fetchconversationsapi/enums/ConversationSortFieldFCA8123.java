package com.yourorg.fetchconversationsapi.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration for the fields by which conversations can be sorted.
 */
public enum ConversationSortFieldFCA8123 {
    LAST_ACTIVITY("lastActivity"),
    SEEN("seen");

    private final String value;

    ConversationSortFieldFCA8123(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
```
src/main/java/com/yourorg/fetchconversationsapi/enums/SortOrderFCA8123.java
```java