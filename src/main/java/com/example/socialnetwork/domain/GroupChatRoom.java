package com.example.socialnetwork.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "group_chat_room")
public class GroupChatRoom {

    @Id
    @SequenceGenerator(
            name = "chatRoom_sequence",
            sequenceName = "chatRoom_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chatRoom_sequence"
    )
    private Long id;
    @Column(unique = true)
    private String groupName;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns =@JoinColumn(name = "user_id") )
    private List<User> users;
}
