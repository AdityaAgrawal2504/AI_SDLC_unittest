package com.example.chat.v1.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserC1V1 {
    private String id;
    private String displayName;
    private String avatarUrl;
}
```
```java
// src/main/java/com/example/chat/v1/domain/MessageC1V1.java