package com.example.socialnetwork.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_account")
public class Account extends User{

    @Column(name = "about")
    private String about;
    @Column(name = "photo_uri")
    private String photoUri;

    @Column(name = "status_code")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "reg_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime regDate;
    @Column(name = "birth_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime birthDate;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
    @Column(name = "message_permission")
    private String messagePermission;
    @Column(name = "last_online_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime lastOnlineTime;

    @Column(name = "created_on", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdOn;
    @Column(name = "updated_on", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime updatedOn;

    private boolean online;

    private boolean blocket;




}
