package com.example.socialnetwork.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatReactionDTO {
    private UUID chatId;
    private String username;
    private String emoji;
    private LocalDateTime createdAt;
}
