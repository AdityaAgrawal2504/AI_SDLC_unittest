package com.example.conversation.api.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Defines the available sorting criteria for conversations.
 */
public enum SortByFCA1 {
    RECENCY("recency"),
    SEEN("seen");

    private final String value;

    SortByFCA1(String value) {
        this.value = value;
    }

    /**
     * Gets the string value of the enum, used for serialization.
     *
     * @return The lowercase string value.
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Finds a SortByFCA1 enum from a string value, case-insensitively.
     *
     * @param text The string to parse.
     * @return The matching SortByFCA1 enum.
     * @throws IllegalArgumentException if no match is found.
     */
    public static SortByFCA1 fromValue(String text) {
        return Arrays.stream(SortByFCA1.values())
            .filter(b -> b.value.equalsIgnoreCase(text))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown enum value: " + text));
    }

    /**
     * A Spring Converter to allow case-insensitive binding of request parameters to the SortByFCA1 enum.
     */
    @Component
    public static class StringToSortByFCA1Converter implements Converter<String, SortByFCA1> {
        @Override
        public SortByFCA1 convert(String source) {
            return SortByFCA1.fromValue(source);
        }
    }
}
src/main/java/com/example/conversation/api/config/WebConfigFCA1.java