package com.example.chat.v1.util;

import io.grpc.Context;
import io.grpc.Metadata;

/**
 * Contains constant values used throughout the application.
 */
public final class ConstantsC1V1 {

    private ConstantsC1V1() {}

    public static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);
    
    public static final Context.Key<String> AUTH_USER_ID_CONTEXT_KEY = Context.key("authenticatedUserId");

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String CONVERSATION_ID_PATTERN = "^conv_[a-zA-Z0-9]{24}$";
    public static final String MESSAGE_ID_PATTERN = "^msg_[a-zA-Z0-9]{24}$";
    public static final String UUID_V4_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";

    public static final int MESSAGE_CONTENT_MIN_LENGTH = 1;
    public static final int MESSAGE_CONTENT_MAX_LENGTH = 10000;
}
```
```java
// src/main/java/com/example/chat/v1/util/IdGeneratorC1V1.java