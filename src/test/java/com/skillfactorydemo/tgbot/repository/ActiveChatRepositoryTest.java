package com.skillfactorydemo.tgbot.repository;

import com.skillfactorydemo.tgbot.entity.ActiveChat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ActiveChatRepositoryTest {

    @Autowired
    private ActiveChatRepository activeChatRepository;

    @Test
    public void testRepo_found(){
        Optional<ActiveChat> activeChatByChatId = activeChatRepository.findActiveChatByChatId(12345L);
        assertTrue(activeChatByChatId.isPresent());
        assertEquals(12345L, activeChatByChatId.get().getChatId());
    }

    @Test
    public void testRepo_notFound(){
        Optional<ActiveChat> activeChatByChatId = activeChatRepository.findActiveChatByChatId(54321L);
        assertFalse(activeChatByChatId.isPresent());
    }

}