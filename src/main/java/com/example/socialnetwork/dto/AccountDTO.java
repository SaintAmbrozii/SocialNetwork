package com.example.socialnetwork.dto;

import com.example.socialnetwork.domain.UserStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO extends UserDTO {

    private String about;

    private String phone;

    private String photoUri;

    private UserStatus status;

    private ZonedDateTime regDate;

    private ZonedDateTime birthDate;

    private String country;

    private String city;

    private String messagePermission;

    private ZonedDateTime lastOnlineTime;

    private ZonedDateTime createdOn;

    private ZonedDateTime updatedOn;

    private boolean online;

    private boolean blocket;
}
