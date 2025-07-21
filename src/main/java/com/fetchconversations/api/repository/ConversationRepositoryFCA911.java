package com.fetchconversations.api.repository;

import com.fetchconversations.api.entity.ConversationFCA911;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the Conversation entity.
 */
@Repository
public interface ConversationRepositoryFCA911 extends JpaRepository<ConversationFCA911, UUID>, JpaSpecificationExecutor<ConversationFCA911> {
}
```
```java
// src/main/java/com/fetchconversations/api/repository/spec/ConversationSpecificationFCA911.java