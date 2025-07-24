src/main/java/com/example/mapper/MessageMapper_X3Y4Z.java
package com.example.mapper;

import com.example.dto.MessageDto_S3T4U;
import com.example.model.Message_V7W8X;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper_X3Y4Z {
    MessageMapper_X3Y4Z INSTANCE = Mappers.getMapper(MessageMapper_X3Y4Z.class);

    @Mapping(source = "conversation.id", target = "conversationId")
    @Mapping(source = "sender.id", target = "senderId")
    MessageDto_S3T4U toDto(Message_V7W8X message);
    
    List<MessageDto_S3T4U> toDtoList(List<Message_V7W8X> messages);
}