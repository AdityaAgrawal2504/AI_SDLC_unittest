package com.example.usersearch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(
    name = "blocked_users",
    uniqueConstraints = @UniqueConstraint(columnNames = {"blocker_id", "blocked_id"})
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(BlockedUserEntity_A0B1.BlockedUserPK.class)
public class BlockedUserEntity_A0B1 {

    @Id
    @Column(name = "blocker_id")
    private UUID blockerId;

    @Id
    @Column(name = "blocked_id")
    private UUID blockedId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BlockedUserPK implements Serializable {
        private UUID blockerId;
        private UUID blockedId;
    }
}
```
src/main/java/com/example/usersearch/model/ConversationParticipantEntity_A0B1.java
```java