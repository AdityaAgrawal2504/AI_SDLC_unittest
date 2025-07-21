package com.fetchmessagesapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

/**
 * JPA Entity representing the link between a user and a conversation.
 */
@Entity
@Table(name = "conversation_participants", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"conversation_id", "user_id"})
})
@Getter
@Setter
public class ConversationParticipantFMA1 {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private ConversationFMA1 conversation;

    @Column(name = "user_id", nullable = false)
    private UUID userId;
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/entity/MessageFMA1.java