package com.example.socialnetwork.dto;

import com.example.socialnetwork.domain.Account;
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

    public static AccountDTO toDto(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAbout(account.getAbout());
        accountDTO.setCity(account.getCity());
        accountDTO.setCountry(account.getCountry());
        accountDTO.setBirthDate(account.getBirthDate());
        accountDTO.setCreatedOn(account.getCreatedOn());
        accountDTO.setLastOnlineTime(account.getLastOnlineTime());
        accountDTO.setPhone(account.getPhone());
        accountDTO.setPhotoUri(accountDTO.getPhotoUri());
        accountDTO.setRegDate(account.getRegDate());
        accountDTO.setUpdatedOn(account.getUpdatedOn());
        return accountDTO;
    }
}
