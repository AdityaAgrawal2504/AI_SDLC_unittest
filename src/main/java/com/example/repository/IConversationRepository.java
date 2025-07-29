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
public interface IConversationRepository extends JpaRepository<Conversation, UUID> {

    @Query("SELECT c FROM Conversation c JOIN c.participants p1 JOIN c.participants p2 WHERE p1.id = :user1Id AND p2.id = :user2Id AND SIZE(c.participants) = 2")
    Optional<Conversation> findPrivateConversationBetweenUsers(@Param("user1Id") UUID user1Id, @Param("user2Id") UUID user2Id);

    @Query(value = "SELECT c FROM Conversation c JOIN c.participants p WHERE p.id = :userId",
           countQuery = "SELECT count(c) FROM Conversation c JOIN c.participants p WHERE p.id = :userId")
    Page<Conversation> findByParticipantId(@Param("userId") UUID userId, Pageable pageable);
}