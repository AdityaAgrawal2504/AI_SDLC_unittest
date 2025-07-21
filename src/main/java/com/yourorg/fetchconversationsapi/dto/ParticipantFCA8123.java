package com.yourorg.fetchconversationsapi.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for a participant in a conversation.
 */
@Data
@Builder
public class ParticipantFCA8123 {
    private String userId;
    private String displayName;
    private String avatarUrl;
}
```
src/main/java/com/yourorg/fetchconversationsapi/dto/ConversationFCA8123.java
```java