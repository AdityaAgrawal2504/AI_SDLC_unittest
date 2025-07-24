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

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    @Query("SELECT c FROM Conversation c JOIN c.participants p WHERE p.userId = :userId")
    Page<Conversation> findByParticipantId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT c FROM Conversation c " +
           "JOIN c.participants p1 ON p1.userId = :userId1 " +
           "JOIN c.participants p2 ON p2.userId = :userId2 " +
           "WHERE (SELECT COUNT(p3.userId) FROM c.participants p3) = 2")
    Optional<Conversation> findDirectConversationBetweenUsers(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);
}