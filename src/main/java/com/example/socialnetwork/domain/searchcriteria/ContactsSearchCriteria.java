package com.example.socialnetwork.domain.searchcriteria;

import com.example.socialnetwork.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactsSearchCriteria {

    private Long id;

    private List<Long> ids;

    private Long idFrom;

    private UserStatus status;

    private Long idTo;

    private String firstName;

    private ZonedDateTime birthDateFrom;

    private ZonedDateTime birthDateTo;

    private String city;

    private String country;

    private Long ageFrom;

    private Long ageTo;


    public ContactsSearchCriteria(Long idFrom, UserStatus status, Long idTo) {
        this.idFrom = idFrom;
        this.status = status;
        this.idTo = idTo;
    }

    public ContactsSearchCriteria(Long idFrom, UserStatus status) {
        this.idFrom = idFrom;
        this.status = status;
    }

    public ContactsSearchCriteria(UserStatus status) {
        this.status = status;
    }

}
