src/main/java/com/example/repository/IParticipantRepository_V3W2X.java
package com.example.repository;

import com.example.model.Participant_S5T6U;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IParticipantRepository_V3W2X extends JpaRepository<Participant_S5T6U, UUID> {
    boolean existsByUserIdAndConversationId(UUID userId, UUID conversationId);
    
    Optional<Participant_S5T6U> findByUserIdAndConversationId(UUID userId, UUID conversationId);

    @Query("SELECT COUNT(m) FROM Message_V7W8X m " +
           "WHERE m.conversation.id = :conversationId AND m.createdAt > " +
           "(SELECT p.lastReadAt FROM Participant_S5T6U p WHERE p.user.id = :userId AND p.conversation.id = :conversationId)")
    long countUnreadMessagesForUserInConversation(@Param("userId") UUID userId, @Param("conversationId") UUID conversationId);
}