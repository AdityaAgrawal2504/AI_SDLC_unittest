src/main/java/com/example/model/ConversationParticipant.java
package com.example.model;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Join entity for the many-to-many relationship between User and Conversation.
 */
@Entity
@Data
@Table(name = "conversation_participants")
@IdClass(ConversationParticipant.ConversationParticipantId.class)
public class ConversationParticipant {

    @Id
    private UUID userId;

    @Id
    private UUID conversationId;

    @UpdateTimestamp
    private OffsetDateTime lastReadAt;

    @Data
    public static class ConversationParticipantId implements Serializable {
        private UUID userId;
        private UUID conversationId;
    }
}