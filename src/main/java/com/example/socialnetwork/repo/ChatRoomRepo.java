package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepo extends JpaRepository<ChatRoom,Long> {

    @Query("""
            SELECT cr FROM ChatRoom cr 
            WHERE cr.sender.id=:sender
            AND cr.recipient.id=:recipient  
            """
    )
    Optional<ChatRoom> findBySenderAndRecipient(Long sender, Long recipient);
}


