src/main/java/com/example/repository/IConversationParticipantRepository.java
package com.example.repository;

import com.example.model.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Data access interface for the join table between Users and Conversations.
 */
@Repository
public interface IConversationParticipantRepository extends JpaRepository<ConversationParticipant, ConversationParticipant.ConversationParticipantId> {

    /**
     * Finds a conversation participant record by user and conversation IDs.
     * @param userId The ID of the user.
     * @param conversationId The ID of the conversation.
     * @return An Optional containing the ConversationParticipant if found.
     */
    Optional<ConversationParticipant> findByUserIdAndConversationId(UUID userId, UUID conversationId);
}