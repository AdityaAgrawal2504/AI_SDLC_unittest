package com.example.chat.v1.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IdGeneratorC1V1Test {

    private IdGeneratorC1V1 idGenerator;

    private static final String ALPHABET_REGEX = "[0-9a-zA-Z]{24}";
    private static final int ID_LENGTH_WITHOUT_PREFIX = 24;

    @BeforeEach
    void setUp() {
        idGenerator = new IdGeneratorC1V1();
    }

    @Test
    void newId_GeneratesCorrectFormat() {
        String prefix = "test";
        String id = idGenerator.newId(prefix);
        assertNotNull(id);
        assertTrue(id.startsWith(prefix + "_"));
        assertEquals(prefix.length() + 1 + ID_LENGTH_WITHOUT_PREFIX, id.length());
        assertTrue(id.substring(prefix.length() + 1).matches(ALPHABET_REGEX));
    }

    @Test
    void newMessageId_GeneratesCorrectFormat() {
        String id = idGenerator.newMessageId();
        assertNotNull(id);
        assertTrue(id.startsWith("msg_"));
        assertEquals("msg_".length() + ID_LENGTH_WITHOUT_PREFIX, id.length());
        assertTrue(id.substring("msg_".length()).matches(ALPHABET_REGEX));
    }

    @Test
    void newConversationId_GeneratesCorrectFormat() {
        String id = idGenerator.newConversationId();
        assertNotNull(id);
        assertTrue(id.startsWith("conv_"));
        assertEquals("conv_".length() + ID_LENGTH_WITHOUT_PREFIX, id.length());
        assertTrue(id.substring("conv_".length()).matches(ALPHABET_REGEX));
    }

    @Test
    void newUserId_GeneratesCorrectFormat() {
        String id = idGenerator.newUserId();
        assertNotNull(id);
        assertTrue(id.startsWith("user_"));
        assertEquals("user_".length() + ID_LENGTH_WITHOUT_PREFIX, id.length());
        assertTrue(id.substring("user_".length()).matches(ALPHABET_REGEX));
    }

    @Test
    void newId_GeneratesUniqueIds() {
        String prefix = "unique";
        String id1 = idGenerator.newId(prefix);
        String id2 = idGenerator.newId(prefix);
        assertNotEquals(id1, id2);

        // Generate a few more to check randomness
        String id3 = idGenerator.newId(prefix);
        assertNotEquals(id1, id3);
        assertNotEquals(id2, id3);
    }
}
```
```java
// src/test/java/com/example/chat/v1/util/ProtoMapperC1V1Test.java