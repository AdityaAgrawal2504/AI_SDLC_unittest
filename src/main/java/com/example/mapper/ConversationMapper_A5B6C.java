src/main/java/com/example/mapper/ConversationMapper_A5B6C.java

<ctrl60>
<ctrl62>
<ctrl61>
package com.example.mapper;

import com.example.dto.ConversationSummaryDto_V5W6X;
import com.example.model.Conversation_P3Q4R;
import com.example.model.Participant_S5T6U;
import com.example.model.User_M1N2O;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper_U1V2W.class, MessageMapper_X3Y4Z.class})
public interface ConversationMapper_A5B6C {

    @Mapping(source = "conversation.participants", target = "participants", qualifiedByName = "participantsToUserDtos")
    @Mapping(target = "lastMessage", expression = "java(messageMapper.toDto(conversation.getMessages().isEmpty() ? null : conversation.getMessages().get(0)))")
    @Mapping(target = "unreadCount", ignore = true) // This will be set manually in the service
    ConversationSummaryDto_V5W6X toSummaryDto(Conversation_P3Q4R conversation, MessageMapper_X3Y4Z messageMapper);

    @Named("participantsToUserDtos")
    default List<com.example.dto.UserDto_D3E4F> participantsToUserDtos(Set<Participant_S5T6U> participants) {
        if (participants == null) {
            return java.util.Collections.emptyList();
        }
        List<User_M1N2O> users = participants.stream()
                .map(Participant_S5T6U::getUser)
                .collect(Collectors.toList());
        return UserMapper_U1V2W.INSTANCE.toDtoList(users);
    }
}