src/main/java/com/example/service/interfaces/IConversationService_V9W0X.java

<ctrl62>


<ctrl60>
package com.example.service.interfaces;

import com.example.dto.ConversationSummaryDto_V5W6X;
import com.example.model.User_M1N2O;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;
<ctrl61>


public interface IConversationService_V9W0X {
    Page<ConversationSummaryDto_V5W6X> listConversations(User_M1N2O user, String status, Pageable pageable);
    void markAsRead(User_M1N2O user, UUID conversationId);
}