package com.example.repository;

import com.example.model.Participant;
import com.example.model.ParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, ParticipantId> {
    Optional<Participant> findByUserIdAndConversationId(UUID userId, UUID conversationId);
    boolean existsByUserIdAndConversationId(UUID userId, UUID conversationId);
}