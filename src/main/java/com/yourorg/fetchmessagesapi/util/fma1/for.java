package com.yourorg.fetchmessagesapi.util.fma1;

import com.yourorg.fetchmessagesapi.exception.fma1.InvalidParameterExceptionFMA_1;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Base64;

/**
 * Utility class for handling cursor encoding and decoding.
 */
@Component
public class CursorUtilFMA_1 {

    /**
     * Decodes a Base64 encoded cursor string into an OffsetDateTime.
     * @param cursor The Base64 encoded cursor.
     * @return The decoded OffsetDateTime.
     */
    public OffsetDateTime decodeCursor(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return null;
        }
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(cursor);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            return OffsetDateTime.parse(decodedString);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            throw new InvalidParameterExceptionFMA_1("The provided cursor is invalid or expired.");
        }
    }

    /**
     * Encodes an OffsetDateTime into a Base64 cursor string.
     * @param dateTime The timestamp to encode.
     * @return The Base64 encoded cursor string.
     */
    public String encodeCursor(OffsetDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(dateTime.toString().getBytes(StandardCharsets.UTF_8));
    }
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/logging/fma1/LoggableFMA_1.java