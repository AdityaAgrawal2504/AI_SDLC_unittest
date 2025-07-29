package com.example.repository;

import com.example.model.Participant;
import com.example.model.ParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;


/**
 * Repository for Participant entities.
 */
@Repository
public interface IParticipantRepository extends JpaRepository<Participant, ParticipantId> {

    /**
     * Checks if a user is a participant in a given conversation.
     * @param userId The user's ID.
     * @param conversationId The conversation's ID.
     * @return True if the user is a participant, false otherwise.
     */
     @Query("SELECT COUNT(p) > 0 FROM Participant p WHERE p.user.id = :userId AND p.conversation.id = :conversationId")
     boolean isUserParticipantInConversation(@Param("userId") UUID userId, @Param("conversationId") UUID conversationId);
}