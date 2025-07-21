package com.fetchmessagesapi.util;

import com.fetchmessagesapi.exception.InvalidCursorExceptionFMA1;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Base64;

/**
 * Utility for encoding and decoding pagination cursors.
 */
@Component
public class CursorUtilFMA1 {

    /**
     * Encodes an Instant into a Base64 string cursor.
     * @param timestamp The Instant to encode.
     * @return A Base64 encoded string.
     */
    public String encode(Instant timestamp) {
        if (timestamp == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(String.valueOf(timestamp.toEpochMilli()).getBytes());
    }

    /**
     * Decodes a Base64 string cursor into an Instant.
     * @param cursor The Base64 encoded string.
     * @return The decoded Instant.
     * @throws InvalidCursorExceptionFMA1 if the cursor is malformed.
     */
    public Instant decode(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            throw new InvalidCursorExceptionFMA1("Cursor cannot be null or empty.");
        }
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(cursor);
            long epochMilli = Long.parseLong(new String(decodedBytes));
            return Instant.ofEpochMilli(epochMilli);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidCursorExceptionFMA1("Invalid cursor format.");
        }
    }
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/util/JwtUtilFMA1.java