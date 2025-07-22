package com.example.entity;

import com.example.enums.MessageStatus_CHAT_2018;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "messages", indexes = {
    @Index(name = "idx_message_conversation_id", columnList = "conversation_id"),
    @Index(name = "idx_message_idempotency_key", columnList = "idempotencyKey", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MessageEntity_MSG_3010 {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private ConversationEntity_CHAT_2017 conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity_UATH_1016 sender;

    @Column(nullable = false, length = 10000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus_CHAT_2018 status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant timestamp;

    @Column(unique = true, updatable = false)
    private String idempotencyKey;

    public MessageEntity_MSG_3010(ConversationEntity_CHAT_2017 conversation, UserEntity_UATH_1016 sender, String content, String idempotencyKey) {
        this.conversation = conversation;
        this.sender = sender;
        this.content = content;
        this.status = MessageStatus_CHAT_2018.SENT;
        this.idempotencyKey = idempotencyKey;
    }
}
```
```java
//
// Filename: src/main/java/com/example/enums/ConversationSortBy_CHAT_2004.java
//