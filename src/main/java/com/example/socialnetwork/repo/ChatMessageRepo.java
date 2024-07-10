package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatMessageRepo extends JpaRepository<ChatMessage, UUID> {


    List<ChatMessage> findByChatId(String chatId);


    List<ChatMessage> findByGroupId(Long groupId);

    Optional<ChatMessage> findByChatMessageId(UUID chatMessageId);
}
