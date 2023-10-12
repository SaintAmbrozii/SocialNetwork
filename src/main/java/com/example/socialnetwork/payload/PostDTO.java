package com.example.socialnetwork.payload;

import com.example.socialnetwork.domain.Image;
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
    @Column(name ="text",length = 4096)
    private String text;
    private String username;
    private String lastname;
    private Long userId;
    private List<Image> images;
    private Integer likes;
    private Integer disLikes;
    private LocalDateTime dateTime;
}

