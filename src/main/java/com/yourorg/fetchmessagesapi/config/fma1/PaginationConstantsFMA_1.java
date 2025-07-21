package com.yourorg.fetchmessagesapi.config.fma1;

/**
 * Defines constants related to pagination.
 */
public final class PaginationConstantsFMA_1 {
    private PaginationConstantsFMA_1() {}

    public static final String DEFAULT_LIMIT = "50";
    public static final int MIN_LIMIT = 1;
    public static final int MAX_LIMIT = 100;
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/util/fma1/CursorUtilFMA_1.java