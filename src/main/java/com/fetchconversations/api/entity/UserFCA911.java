package com.fetchconversations.api.entity;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class UserFCA911 {
    @Id
    private UUID id;
    private String displayName;
    private String avatarUrl;
}
```
```java
// src/main/java/com/fetchconversations/api/entity/MessageFCA911.java