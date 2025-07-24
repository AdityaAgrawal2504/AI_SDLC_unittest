src/main/java/com/example/model/Session_B1C2D.java
package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
public class Session_B1C2D {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User_M1N2O user;

    @Column(nullable = false, unique = true)
    private String refreshTokenHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}