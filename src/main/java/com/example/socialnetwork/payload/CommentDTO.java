package com.example.socialnetwork.payload;

import com.example.socialnetwork.domain.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;
    @Column(name = "text",length = 2048)
    private String text;
    private String author;
    private Long postId;
}
