package com.example.util;

import com.example.dto.response.UserSignupResponse;
import com.example.model.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DtoMapperTest {

    @Test
    void toUserSignupResponse_shouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        User user = User.builder()
                .id(id)
                .phoneNumber("+123")
                .createdAt(now)
                .build();
        
        UserSignupResponse response = DtoMapper.toUserSignupResponse(user);
        
        assertEquals(id, response.getId());
        assertEquals("+123", response.getPhoneNumber());
        assertEquals(now, response.getCreatedAt());
    }
}