package com.fetchconversations.api.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class ParticipantDtoFCA911 {
    private UUID userId;
    private String displayName;
    private String avatarUrl;
}
```
```java
// src/main/java/com/fetchconversations/api/entity/UserFCA911.java