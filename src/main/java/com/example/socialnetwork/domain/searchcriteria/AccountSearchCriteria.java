package com.example.socialnetwork.domain.searchcriteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountSearchCriteria {


    private List<Long> ids;

    private List<Long> blockedByIds;

    private String author;

    private String firstName;

    private String lastName;

    private String city;

    private String country;

    private Boolean isBlocked;

    private Integer ageTo;

    private Integer ageFrom;

}
