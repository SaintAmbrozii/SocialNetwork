package com.example.socialnetwork.payload;

import com.example.socialnetwork.domain.Comment;
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
    private String text;
    private Long postId;
    private Long userId;
    private String username;
    private String lastname;
    private LocalDateTime dateTime;

    public static CommentDTO toDto(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setUserId(comment.getUserId());
        dto.setUsername(comment.getUsername());
        dto.setLastname(comment.getLastname());
        dto.setText(comment.getText());
        dto.setPostId(comment.getPostId());
        dto.setDateTime(comment.getDateTime());
        return dto;
    }


}
