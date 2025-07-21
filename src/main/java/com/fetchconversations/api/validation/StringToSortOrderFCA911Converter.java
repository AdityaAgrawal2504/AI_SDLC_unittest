package com.fetchconversations.api.validation;

import com.fetchconversations.api.enums.SortOrderFCA911;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a string request parameter to a SortOrderFCA911 enum.
 */
@Component
public class StringToSortOrderFCA911Converter implements Converter<String, SortOrderFCA911> {
    @Override
    public SortOrderFCA911 convert(String source) {
        try {
            return SortOrderFCA911.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid sort order: " + source);
        }
    }
}
```
```java
// src/main/java/com/fetchconversations/api/config/WebConfigFCA911.java