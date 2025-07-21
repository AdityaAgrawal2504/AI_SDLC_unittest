package com.fetchconversations.api.validation;

import com.fetchconversations.api.enums.SortFieldFCA911;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a string request parameter to a SortFieldFCA911 enum.
 */
@Component
public class StringToSortFieldFCA911Converter implements Converter<String, SortFieldFCA911> {
    @Override
    public SortFieldFCA911 convert(String source) {
        try {
            return SortFieldFCA911.fromValue(source);
        } catch (IllegalArgumentException e) {
            // Let the global exception handler catch this for a clean 400 response
            throw e;
        }
    }
}
```
```java
// src/main/java/com/fetchconversations/api/validation/StringToSortOrderFCA911Converter.java