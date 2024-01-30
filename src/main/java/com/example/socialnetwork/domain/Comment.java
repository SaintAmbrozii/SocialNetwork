package com.example.socialnetwork.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "text",length = 8192)
    private String text;
    @Column(name = "postId")
    private Long postId;

    @Column(name = "userId")
    private Long userId;
    @Column(name = "userName")
    private String username;
    @Column(name = "lastName")
    private String lastname;

    @Column(name = "dateTime")
    private LocalDateTime dateTime;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
