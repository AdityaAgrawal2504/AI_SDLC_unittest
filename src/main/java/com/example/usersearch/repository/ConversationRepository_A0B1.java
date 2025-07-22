package com.example.usersearch.repository;

import com.example.usersearch.model.ConversationParticipantEntity_A0B1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConversationRepository_A0B1 extends JpaRepository<ConversationParticipantEntity_A0B1, ConversationParticipantEntity_A0B1.ConversationParticipantPK> {

    /**
     * Checks if a 1-on-1 conversation exists between two users.
     * This query finds conversations that have exactly two participants and both specified user IDs are in it.
     * @param requesterId The ID of the first user (the one searching).
     * @param targetId The ID of the second user (the one in the search results).
     * @return true if a conversation exists, false otherwise.
     */
    @Query(value = """
        SELECT CASE WHEN EXISTS (
            SELECT 1 FROM (
                SELECT cp.conversation_id
                FROM conversation_participants cp
                WHERE cp.user_id IN (:requesterId, :targetId)
                GROUP BY cp.conversation_id
                HAVING COUNT(DISTINCT cp.user_id) = 2
            ) as conv_with_both
            INNER JOIN (
                SELECT conversation_id
                FROM conversation_participants
                GROUP BY conversation_id
                HAVING COUNT(user_id) = 2
            ) as one_on_one_conv ON conv_with_both.conversation_id = one_on_one_conv.conversation_id
        ) THEN TRUE ELSE FALSE END
    """, nativeQuery = true)
    boolean existsConversationBetween(@Param("requesterId") UUID requesterId, @Param("targetId") UUID targetId);
}
```
src/main/java/com/example/usersearch/repository/BlockedUserRepository_A0B1.java
```java