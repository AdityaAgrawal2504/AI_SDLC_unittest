package com.example.usersearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummary_A0B1 {
    private UUID userId;
    private String name;
    private String profilePictureUrl;
    private boolean hasExistingConversation;
}
```
src/main/java/com/example/usersearch/dto/PaginationInfo_A0B1.java
```java