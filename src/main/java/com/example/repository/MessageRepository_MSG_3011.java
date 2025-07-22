package com.example.repository;

import com.example.entity.MessageEntity_MSG_3010;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for MessageEntity.
 */
@Repository
public interface MessageRepository_MSG_3011 extends JpaRepository<MessageEntity_MSG_3010, UUID> {
    
    /**
     * Finds messages for a given conversation, paginated.
     */
    Page<MessageEntity_MSG_3010> findByConversationId(UUID conversationId, Pageable pageable);

    /**
     * Finds a message by its idempotency key.
     */
    Optional<MessageEntity_MSG_3010> findByIdempotencyKey(String idempotencyKey);
    
    /**
     * Counts the number of unread messages for a user in a specific conversation.
     */
    @Query("SELECT count(m) FROM MessageEntity_MSG_3010 m WHERE m.conversation.id = :conversationId AND m.status != 'SEEN' AND m.sender.id != :userId")
    long countUnreadMessages(@Param("conversationId") UUID conversationId, @Param("userId") UUID userId);
    
    /**
     * Updates the status of messages to SEEN for a user in a conversation up to a certain time.
     */
    @Query("UPDATE MessageEntity_MSG_3010 m SET m.status = 'SEEN' " +
           "WHERE m.conversation.id = :conversationId AND m.sender.id != :userId AND m.status != 'SEEN' AND m.timestamp <= :readUpToTimestamp")
    void markMessagesAsRead(@Param("conversationId") UUID conversationId, @Param("userId") UUID userId, @Param("readUpToTimestamp") Instant readUpToTimestamp);
}
```
```java
//
// Filename: src/main/java/com/example/security/JwtAuthenticationFilter_SEC_33CC.java
//