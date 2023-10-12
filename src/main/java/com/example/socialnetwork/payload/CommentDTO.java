package com.example.socialnetwork.payload;

import com.example.socialnetwork.domain.User;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

    private Long id;
    @Column(name = "text",length = 2048)
    private String text;
    private Long postId;
    private Long userId;
    private String username;
    private String lastname;
    private LocalDateTime dateTime;
}
