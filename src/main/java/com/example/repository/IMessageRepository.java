package com.example.repository;

import com.example.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for Message entities.
 */
@Repository
public interface IMessageRepository extends JpaRepository<Message, UUID> {

    /**
     * Finds messages by conversation ID, paginated and ordered by sent time descending.
     * @param conversationId The conversation's ID.
     * @param pageable Pagination information.
     * @return A page of messages.
     */
    Page<Message> findByConversationIdOrderBySentAtDesc(UUID conversationId, Pageable pageable);
}