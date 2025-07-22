package com.example.usersearch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name = "conversation_participants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ConversationParticipantEntity_A0B1.ConversationParticipantPK.class)
public class ConversationParticipantEntity_A0B1 {

    @Id
    @Column(name = "conversation_id")
    private UUID conversationId;

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversationParticipantPK implements Serializable {
        private UUID conversationId;
        private UUID userId;
    }
}
```
src/main/java/com/example/usersearch/repository/UserRepository_A0B1.java
```java