package com.example.socialnetwork.dto;

import com.example.socialnetwork.domain.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserContactDTO {

    private Long id;

    private String fistName;

    private String lastName;

    private String photo;

    private String city;

    private String country;

    private ZonedDateTime birthDate;

    private Boolean isOnline;

    private Long fromAccountId;

    private UserStatus status;

    private Long toAccountId;

    private LocalDateTime created_at;
}
