package com.example.socialnetwork.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_contacts")
@ToString
@Builder
public class UserContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_acc_id")
    private Long fromAccountId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "to_acc_id")
    private Long toAccountId;

    @Column(name = "created_at")
    private LocalDateTime created_at;



}
