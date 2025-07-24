src/main/java/com/example/dto/SendMessageDto_P1Q2R.java
package com.example.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageDto_P1Q2R {
    private UUID conversationId;

    private String recipientPhoneNumber;

    @NotBlank(message = "Content cannot be empty.")
    private String content;

    @AssertTrue(message = "Either conversationId or recipientPhoneNumber must be provided, but not both.")
    private boolean isTargetValid() {
        return (conversationId != null) ^ (recipientPhoneNumber != null && !recipientPhoneNumber.isBlank());
    }
}