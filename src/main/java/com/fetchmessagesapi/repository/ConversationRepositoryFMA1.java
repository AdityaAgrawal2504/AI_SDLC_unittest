package com.fetchmessagesapi.repository;

import com.fetchmessagesapi.entity.ConversationFMA1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for Conversation entities.
 */
@Repository
public interface ConversationRepositoryFMA1 extends JpaRepository<ConversationFMA1, UUID> {
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/repository/MessageRepositoryFMA1.java