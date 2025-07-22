package com.example.chat.v1.domain;

import com.example.chat.v1.grpc.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageC1V1 {
    private String id;
    private String clientMessageId;
    private String conversationId;
    private String senderId;
    private String content;
    private Instant createdAt;
    private MessageStatus status;
}
```
```java
// src/main/java/com/example/chat/v1/exception/ChatValidationExceptionC1V1.java