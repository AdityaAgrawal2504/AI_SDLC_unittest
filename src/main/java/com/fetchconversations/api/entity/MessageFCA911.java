package com.fetchconversations.api.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
public class MessageFCA911 {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private ConversationFCA911 conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserFCA911 sender;

    @Lob
    private String content;

    private OffsetDateTime timestamp;
}
```
```java
// src/main/java/com/fetchconversations/api/entity/ConversationFCA911.java