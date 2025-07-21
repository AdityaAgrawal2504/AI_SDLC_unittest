package com.fetchmessagesapi.repository;

import com.fetchmessagesapi.entity.ConversationParticipantFMA1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for ConversationParticipant entities.
 */
@Repository
public interface ConversationParticipantRepositoryFMA1 extends JpaRepository<ConversationParticipantFMA1, Long> {

    /**
     * Checks if a participant with the given conversation ID and user ID exists.
     * @param conversationId The ID of the conversation.
     * @param userId The ID of the user.
     * @return True if the user is a participant, false otherwise.
     */
    boolean existsByConversationIdAndUserId(UUID conversationId, UUID userId);
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/repository/ConversationRepositoryFMA1.java