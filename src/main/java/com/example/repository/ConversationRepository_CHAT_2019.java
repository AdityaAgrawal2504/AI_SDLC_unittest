package com.example.repository;

import com.example.entity.ConversationEntity_CHAT_2017;
import com.example.entity.UserEntity_UATH_1016;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * JPA repository for ConversationEntity.
 */
@Repository
public interface ConversationRepository_CHAT_2019 extends JpaRepository<ConversationEntity_CHAT_2017, UUID> {

    /**
     * Finds conversations for a specific user, with optional search.
     */
    @Query("SELECT c FROM ConversationEntity_CHAT_2017 c JOIN c.participants p WHERE p.id = :userId " +
           "AND (:search IS NULL OR " +
           "EXISTS (SELECT p2 FROM c.participants p2 WHERE p2.id != :userId AND lower(p2.displayName) LIKE lower(concat('%', :search, '%'))) OR " +
           "EXISTS (SELECT m FROM MessageEntity_MSG_3010 m WHERE m.conversation = c AND lower(m.content) LIKE lower(concat('%', :search, '%'))))")
    Page<ConversationEntity_CHAT_2017> findConversationsForUser(@Param("userId") UUID userId, @Param("search") String search, Pageable pageable);

    /**
     * Finds a 1-on-1 conversation between two specific users.
     */
    @Query("SELECT c FROM ConversationEntity_CHAT_2017 c " +
           "WHERE (SELECT COUNT(p) FROM c.participants p WHERE p IN :participants) = 2 " +
           "AND (SELECT COUNT(p) FROM c.participants p) = 2")
    Optional<ConversationEntity_CHAT_2017> findByExactParticipants(@Param("participants") Set<UserEntity_UATH_1016> participants);
}
```
```java
//
// Filename: src/main/java/com/example/repository/MessageRepository_MSG_3011.java
//