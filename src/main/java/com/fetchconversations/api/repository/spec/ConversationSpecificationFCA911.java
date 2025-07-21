package com.fetchconversations.api.repository.spec;

import com.fetchconversations.api.entity.ConversationFCA911;
import com.fetchconversations.api.entity.ConversationParticipantFCA911;
import com.fetchconversations.api.entity.MessageFCA911;
import com.fetchconversations.api.entity.UserFCA911;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.UUID;

/**
 * Provides Specifications for creating dynamic queries for Conversations.
 */
public class ConversationSpecificationFCA911 {

    /**
     * Creates a specification to find conversations for a specific user.
     * @param userId The ID of the user.
     * @return A Specification for the query.
     */
    public static Specification<ConversationFCA911> forUser(UUID userId) {
        return (root, query, cb) -> {
            Subquery<UUID> subquery = query.subquery(UUID.class);
            Root<ConversationParticipantFCA911> participantRoot = subquery.from(ConversationParticipantFCA911.class);
            subquery.select(participantRoot.get("conversation").get("id"))
                .where(cb.equal(participantRoot.get("user").get("id"), userId));
            return root.get("id").in(subquery);
        };
    }

    /**
     * Creates a specification to filter conversations by a search query.
     * The query searches in participant names and message content.
     * @param searchTerm The string to search for.
     * @return A Specification for the query.
     */
    public static Specification<ConversationFCA911> withSearchQuery(String searchTerm) {
        if (!StringUtils.hasText(searchTerm)) {
            return null;
        }
        String likePattern = "%" + searchTerm.toLowerCase() + "%";

        return (root, query, cb) -> {
            // Join for participant display names
            Join<ConversationFCA911, ConversationParticipantFCA911> participantJoin = root.join("participants");
            Join<ConversationParticipantFCA911, UserFCA911> userJoin = participantJoin.join("user");
            Predicate participantNameMatch = cb.like(cb.lower(userJoin.get("displayName")), likePattern);

            // Subquery for message content
            Subquery<UUID> messageSubquery = query.subquery(UUID.class);
            Root<MessageFCA911> messageRoot = messageSubquery.from(MessageFCA911.class);
            messageSubquery.select(messageRoot.get("conversation").get("id"))
                .where(cb.like(cb.lower(messageRoot.get("content")), likePattern));
            Predicate messageContentMatch = root.get("id").in(messageSubquery);

            return cb.or(participantNameMatch, messageContentMatch);
        };
    }
}
```
```java
// src/main/java/com/fetchconversations/api/validation/StringToSortFieldFCA911Converter.java