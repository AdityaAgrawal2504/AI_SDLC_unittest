src/main/java/com/example/service/interfaces/IMessageService_B3C4D.java
package com.example.service.interfaces;

import com.example.dto.MessageDto_S3T4U;
import com.example.dto.SendMessageDto_P1Q2R;
import com.example.model.User_M1N2O;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface IMessageService_B3C4D {
    MessageDto_S3T4U sendMessage(User_M1N2O sender, SendMessageDto_P1Q2R sendMessageDto);
    Page<MessageDto_S3T4U> listMessagesInConversation(User_M1N2O user, UUID conversationId, Pageable pageable);
    Page<MessageDto_S3T4U> searchMessages(User_M1N2O user, String query, Pageable pageable);
}