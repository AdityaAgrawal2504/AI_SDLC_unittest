package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the participation of a user in a conversation (join table).
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "participants")
@IdClass(ParticipantId.class)
public class Participant {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
}