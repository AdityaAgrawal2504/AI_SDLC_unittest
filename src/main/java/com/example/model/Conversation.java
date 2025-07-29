package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a conversation between two or more users.
 *
 * <!--
 * mermaid
 * erDiagram
 *     Conversation {
 *         UUID id PK
 *         timestamp createdAt
 *         timestamp updatedAt
 *     }
 *     Participant {
 *         UUID userId FK
 *         UUID conversationId FK
 *     }
 *     Message {
 *         UUID id PK
 *         UUID conversationId FK
 *     }
 *     Conversation ||--o{ Participant : "has"
 *     Conversation ||--o{ Message : "contains"
 * -->
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}