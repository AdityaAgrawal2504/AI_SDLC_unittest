package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

/**
 * Composite primary key for the Participant entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantId implements Serializable {
    private UUID user;
    private UUID conversation;
}