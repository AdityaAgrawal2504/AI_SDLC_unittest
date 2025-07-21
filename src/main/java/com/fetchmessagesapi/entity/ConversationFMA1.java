package com.fetchmessagesapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * JPA Entity representing a conversation.
 */
@Entity
@Table(name = "conversations")
@Getter
@Setter
public class ConversationFMA1 {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "conversation")
    private Set<ConversationParticipantFMA1> participants = new HashSet<>();
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/entity/ConversationParticipantFMA1.java