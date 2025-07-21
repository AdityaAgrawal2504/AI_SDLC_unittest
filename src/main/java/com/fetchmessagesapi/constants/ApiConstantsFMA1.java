package com.fetchmessagesapi.constants;

/**
 * Defines constant values used across the application.
 */
public final class ApiConstantsFMA1 {

    private ApiConstantsFMA1() {}

    public static final String API_VERSION = "/v1";
    public static final String CONVERSATIONS_ENDPOINT = API_VERSION + "/conversations";
    public static final String MESSAGES_ENDPOINT = "/{conversationId}/messages";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int DEFAULT_LIMIT = 50;
    public static final int MAX_LIMIT = 100;

}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/enums/ContentTypeFMA1.java