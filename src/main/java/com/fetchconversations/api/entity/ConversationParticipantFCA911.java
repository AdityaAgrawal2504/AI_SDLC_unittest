package com.fetchconversations.api.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@IdClass(ConversationParticipantIdFCA911.class)
public class ConversationParticipantFCA911 {

    @Id
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private ConversationFCA911 conversation;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserFCA911 user;

    private int unreadCount;

    private boolean isMuted;

    private boolean lastMessageSeen;
}

@Data
class ConversationParticipantIdFCA911 implements Serializable {
    private UUID conversation;
    private UUID user;
}
```
```java
// src/main/java/com/fetchconversations/api/repository/ConversationRepositoryFCA911.java