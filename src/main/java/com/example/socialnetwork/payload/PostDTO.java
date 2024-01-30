package com.example.socialnetwork.payload;

import com.example.socialnetwork.domain.Image;
import com.example.socialnetwork.domain.Post;
import com.example.socialnetwork.domain.User;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostDTO {

    private Long id;
    private String text;
    private String title;
    private String username;
    private String lastname;
    private Long userId;
    private List<Image> images;
    private Integer likes;
    private Integer disLikes;
    private LocalDateTime dateTime;

    public static PostDTO toDto(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setUserId(post.getUserId());
        postDTO.setUsername(postDTO.getUsername());
        postDTO.setLastname(post.getLastname());
        postDTO.setText(post.getText());
        postDTO.setDateTime(post.getDateTime());
        postDTO.setLikes(post.getLikes());
        postDTO.setDisLikes(post.getDisLikes());
        postDTO.setImages(post.getImages());
        return postDTO;
    }
}

