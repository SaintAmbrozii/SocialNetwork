package com.example.socialnetwork.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat_reaction")
public class ChatReaction {

    @EmbeddedId
    private ChatReactionId chatReactionId;


    @ManyToOne
    @JoinColumn(name = "chatMessageId")
    private ChatMessage chatMessage;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private EMOJI emoji;

    private LocalDateTime createdAt;
}
