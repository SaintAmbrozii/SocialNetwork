package com.example.socialnetwork.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class ChatReactionId implements Serializable {



    private UUID chatMessage;


    private Long user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatReactionId)) return false;
        ChatReactionId that = (ChatReactionId) o;
        return Objects.equals(chatMessage, that.chatMessage) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatMessage, user);
    }
}
