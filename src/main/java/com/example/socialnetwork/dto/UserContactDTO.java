package com.example.socialnetwork.dto;

import com.example.socialnetwork.domain.UserContact;
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

    public static UserContactDTO toDto(UserContact userContact) {
        UserContactDTO dto = new UserContactDTO();
        dto.setId(userContact.getId());
        dto.setStatus(userContact.getStatus());
        dto.setFromAccountId(userContact.getFromAccountId());
        dto.setToAccountId(userContact.getToAccountId());
        return dto;
    }

    public static UserContactDTO toAccountDto(AccountDTO accountDTO) {
        UserContactDTO contactDTO = new UserContactDTO();
        contactDTO.setId(accountDTO.getId());
        contactDTO.setStatus(accountDTO.getStatus());
        contactDTO.setFistName(accountDTO.getName());
        contactDTO.setLastName(accountDTO.getLastname());
        contactDTO.setCountry(accountDTO.getCountry());
        contactDTO.setCity(accountDTO.getCity());
        contactDTO.setPhoto(accountDTO.getPhotoUri());
        contactDTO.setBirthDate(accountDTO.getBirthDate());
        return contactDTO;
    }




}
