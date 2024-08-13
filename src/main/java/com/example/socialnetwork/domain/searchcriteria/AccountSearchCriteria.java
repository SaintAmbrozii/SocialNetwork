package com.example.socialnetwork.domain.searchcriteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountSearchCriteria {

    private Long id;

    private List<Long> ids;

    private List<Long> blockedByIds;

    private String author;

    private String name;

    private String lastName;

    private String city;

    private String country;

    private Boolean isBlocked;

    private Integer ageTo;

    private Integer ageFrom;


    public static AccountSearchCriteria toContactSearchCriteria(ContactsSearchCriteria criteria) {
        AccountSearchCriteria searchCriteria = new AccountSearchCriteria();
        searchCriteria.setId(criteria.getId());
        searchCriteria.setIds(criteria.getIds());
        searchCriteria.setName(criteria.getFirstName());
        searchCriteria.setCity(criteria.getCity());
        searchCriteria.setCountry(criteria.getCountry());
        return searchCriteria;
    }




}
