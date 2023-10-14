package com.example.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @Column(name = "text",length = 4096)
    private String text;

    @Column(name = "username")
    private String username;

    @Column(name = "lastname")
    private String lastname;
    @Column(name = "userId")
    private Long userId;
    @Column(name = "dateTime")
    private LocalDateTime dateTime;




    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "post_images",joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<Image> images = new ArrayList<>();



    private Integer likes = 0;

    private Integer disLikes = 0;

    public void plusLikes() {
        likes++;
    }

    public void minusLikes() {
        if (likes > 0)
            likes--;
    }

    public void plusDislikes() {
        disLikes++;
    }

    public void minusDislikes() {
        if (disLikes > 0)
            disLikes--;
    }
    public void addImage(Image image) {
        images.add(image);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }







}
