src/main/java/com/example/dto/UserDto_D3E4F.java
package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto_D3E4F {
    private UUID id;
    private String phoneNumber;
    private LocalDateTime createdAt;
}