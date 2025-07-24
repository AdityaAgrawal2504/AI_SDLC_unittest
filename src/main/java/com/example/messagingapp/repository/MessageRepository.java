package com.example.messagingapp.repository;

import com.example.messagingapp.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    /**
     * Finds messages where the user is either the sender or the recipient, with an optional content search.
     * @param userId The ID of the user.
     * @param searchTerm A string to search for in the message content. Can be null or empty.
     * @param pageable Pagination and sorting information.
     * @return A page of messages.
     */
    @Query("SELECT m FROM Message m WHERE (m.sender.id = :userId OR m.recipient.id = :userId) AND (:searchTerm IS NULL OR lower(m.content) LIKE lower(concat('%', :searchTerm, '%')))")
    Page<Message> findMessagesForUser(@Param("userId") UUID userId, @Param("searchTerm") String searchTerm, Pageable pageable);
}