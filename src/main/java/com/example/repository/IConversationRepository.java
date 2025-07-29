package com.example.repository;

import com.example.model.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Conversation entities.
 */
@Repository
public interface IConversationRepository extends JpaRepository<Conversation, UUID> {

    /**
     * Finds conversations for a given user, paginated.
     * @param userId The user's ID.
     * @param pageable Pagination information.
     * @return A page of conversations.
     */
    @Query("SELECT c FROM Conversation c JOIN c.participants p WHERE p.user.id = :userId ORDER BY c.updatedAt DESC")
    Page<Conversation> findByUserId(@Param("userId") UUID userId, Pageable pageable);

    /**
     * Finds a conversation between two specific users.
     * @param userId1 ID of the first user.
     * @param userId2 ID of the second user.
     * @return An optional containing the conversation if it exists.
     */
    @Query("SELECT c FROM Conversation c " +
           "WHERE (SELECT COUNT(p) FROM Participant p WHERE p.conversation = c AND p.user.id IN (:userId1, :userId2)) = 2 " +
           "AND (SELECT COUNT(p) FROM Participant p WHERE p.conversation = c) = 2")
    Optional<Conversation> findConversationBetweenUsers(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);
}