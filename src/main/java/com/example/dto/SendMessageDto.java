src/main/java/com/example/dto/SendMessageDto.java
package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO for sending a message.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageDto {
    @NotBlank(message = "Recipient phone number is required.")
    private String recipientPhoneNumber;

    @NotBlank(message = "Content cannot be empty.")
    @Size(min = 1, max = 5000, message = "Content must be between 1 and 5000 characters.")
    private String content;
}