src/main/java/com/example/dto/UserDto.java
package com.example.dto;

import com.example.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Public representation of a user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String phoneNumber;
    private OffsetDateTime createdAt;

    /**
     * Maps a User entity to a UserDto.
     * @param user The User entity.
     * @return A UserDto instance.
     */
    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getPhoneNumber(),
                user.getCreatedAt()
        );
    }
}