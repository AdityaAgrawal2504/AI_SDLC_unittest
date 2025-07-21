package com.mycompany.chatapp.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * Utility for sanitizing user-provided content.
 */
public final class SanitizationUtil_RealTimeChatStreamAPI_1122 {

    private SanitizationUtil_RealTimeChatStreamAPI_1122() {}

    /**
     * Sanitizes a string to remove potential XSS vectors.
     * It also trims leading/trailing whitespace.
     * @param untrustedInput The raw string from the user.
     * @return A sanitized, safe string.
     */
    public static String sanitize(String untrustedInput) {
        if (untrustedInput == null) {
            return "";
        }
        String trimmed = untrustedInput.trim();
        return Jsoup.clean(trimmed, Safelist.none());
    }
}
```
```java
// src/main/java/com/mycompany/chatapp/validator/RequestValidator_RealTimeChatStreamAPI_1122.java