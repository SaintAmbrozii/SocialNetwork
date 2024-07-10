package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.ChatReaction;
import com.example.socialnetwork.domain.ChatReactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatReactionRepo extends JpaRepository<ChatReaction, ChatReactionId> {

    @Query("SELECT cr FROM ChatReaction cr WHERE cr.chatMessage.id=:chatMessageId")
    List<ChatReaction> findByChatMessage(UUID chatMessageId);


    @Query("""
            SELECT cr FROM ChatReaction cr
            WHERE cr.chatMessage.id=:chatMessageId
            AND cr.user.username=:username
            """
    )
    Optional<ChatReaction> findByChatMessageAndUser(UUID chatMessageId, String username);
}
