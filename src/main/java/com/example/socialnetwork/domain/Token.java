package com.example.socialnetwork.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class Token {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String token;


    private boolean expired = false;

    private boolean revoked = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;




}
