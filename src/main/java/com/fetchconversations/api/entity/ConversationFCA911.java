package com.fetchconversations.api.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
public class ConversationFCA911 {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    private String title;

    private String avatarUrl;

    @OneToMany(mappedBy = "conversation")
    private Set<ConversationParticipantFCA911> participants;

    @OneToOne
    @JoinColumn(name = "last_message_id")
    private MessageFCA911 lastMessage;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
```
```java
// src/main/java/com/fetchconversations/api/entity/ConversationParticipantFCA911.java