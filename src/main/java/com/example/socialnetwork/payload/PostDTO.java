package com.example.socialnetwork.payload;

import com.example.socialnetwork.domain.Image;
import com.example.socialnetwork.domain.User;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {

    private Long id;
    @Column(name ="text",length = 4096)
    private String text;
    private String username;
    private List<Image> images;
    private Integer likes;
    private Integer disLikes;
}

