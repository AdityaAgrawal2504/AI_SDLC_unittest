package com.fetchmessagesapi.entity;

import com.fetchmessagesapi.enums.ContentTypeFMA1;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.UUID;

/**
 * JPA Entity representing a message.
 */
@Entity
@Table(name = "messages", indexes = {
    @Index(name = "idx_message_conversation_created", columnList = "conversationId, createdAt DESC")
})
@Getter
@Setter
public class MessageFMA1 {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID conversationId;

    @Column(nullable = false)
    private UUID senderId;

    @Column(nullable = false)
    private String senderDisplayName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.fetchmessagesapi.enums.SenderTypeFMA1 senderType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentTypeFMA1 contentType;

    @Lob
    private String textContent;

    private String url;

    // Metadata fields
    private String metadataFileName;
    private Long metadataFileSize;
    private String metadataMimeType;
    private Integer metadataWidth;
    private Integer metadataHeight;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
```
```java
// FILENAME: src/main/java/com/fetchmessagesapi/exception/ApiBaseExceptionFMA1.java