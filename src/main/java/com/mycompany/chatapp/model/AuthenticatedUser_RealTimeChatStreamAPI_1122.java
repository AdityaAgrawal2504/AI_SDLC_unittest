package com.mycompany.chatapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a user that has been authenticated via a session token.
 */
@Getter
@AllArgsConstructor
public class AuthenticatedUser_RealTimeChatStreamAPI_1122 {
    private final String userId;
    private final String displayName;
}
```
```java
// src/main/java/com/mycompany/chatapp/model/ChatSessionContext_RealTimeChatStreamAPI_1122.java