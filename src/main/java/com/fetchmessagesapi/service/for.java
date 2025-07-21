package com.fetchmessagesapi.service;

import com.fetchmessagesapi.dto.MessageListResponseFMA1;

import java.util.UUID;

/**
 * Service interface for message-related operations.
 */
public interface MessageServiceFMA1 {
    /**
     * Retrieves a paginated list of messages for a given conversation.
     *
     * @param conversationId The ID of the conversation.
     * @param limit The maximum number of messages to return.
     * @param cursor The pagination cursor from a previous request.
     * @param userId The ID of the authenticated user making the request.
     * @return A MessageListResponseFMA1 containing the messages and pagination info.
     */
    MessageListResponseFMA1 fetchMessages(UUID conversationId, int limit, String cursor, UUID userId);
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/service/MessageServiceImplFMA1.java