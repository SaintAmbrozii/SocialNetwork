package com.example.socialnetwork.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient")
    private User recipient;

    private String text;
    private String fileName;
    private String fileUrl;
    private String type;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupChatRoom groupId;

    @OneToMany(mappedBy = "chatMessage", fetch = FetchType.EAGER)
    private List<ChatReaction> chatReactionList;
}
