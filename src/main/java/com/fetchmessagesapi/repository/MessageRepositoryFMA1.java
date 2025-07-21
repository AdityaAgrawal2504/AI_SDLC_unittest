package com.fetchmessagesapi.repository;

import com.fetchmessagesapi.entity.MessageFMA1;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

/**
 * Spring Data JPA repository for Message entities.
 */
@Repository
public interface MessageRepositoryFMA1 extends JpaRepository<MessageFMA1, UUID> {

    /**
     * Finds messages for a given conversation, ordered by creation time descending.
     * @param conversationId The ID of the conversation.
     * @param pageable Pagination information (limit and sort).
     * @return A slice of messages.
     */
    Slice<MessageFMA1> findByConversationIdOrderByCreatedAtDesc(UUID conversationId, Pageable pageable);

    /**
     * Finds messages for a given conversation created before a certain timestamp, ordered by creation time descending.
     * @param conversationId The ID of the conversation.
     * @param createdAt The cursor timestamp.
     * @param pageable Pagination information (limit and sort).
     * @return A slice of messages.
     */
    Slice<MessageFMA1> findByConversationIdAndCreatedAtBeforeOrderByCreatedAtDesc(UUID conversationId, Instant createdAt, Pageable pageable);
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/util/CursorUtilFMA1.java