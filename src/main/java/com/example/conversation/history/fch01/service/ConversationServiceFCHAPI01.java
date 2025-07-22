package com.example.conversation.history.fch01.service;

import com.example.conversation.history.fch01.dto.PaginatedMessagesResponseFCHAPI01;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Interface defining the business logic for conversation operations.
 */
public interface ConversationServiceFCHAPI01 {

    /**
     * Retrieves a paginated history of messages for a single conversation.
     *
     * @param conversationId The unique identifier of the conversation.
     * @param limit The maximum number of messages to return.
     * @param before A cursor for pagination, fetching messages created before this timestamp.
     * @return A paginated list of messages.
     */
    PaginatedMessagesResponseFCHAPI01 fetchConversationHistory(UUID conversationId, int limit, OffsetDateTime before);
}
src/main/java/com/example/conversation/history/fch01/service/ConversationServiceImplFCHAPI02.java