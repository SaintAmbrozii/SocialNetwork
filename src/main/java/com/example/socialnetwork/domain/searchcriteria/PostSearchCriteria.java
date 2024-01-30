package com.example.socialnetwork.domain.searchcriteria;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.time.LocalDateTime;


@Data
@ToString
@Slf4j
public class PostSearchCriteria {

    private String title;
    private String username;
    private String lastname;
    private LocalDateTime dateTime;

    private int page = 0;
    private int count = 10;

    private Sort.Direction direction = Sort.Direction.DESC;
    private String sortProperty = "id";

    public Pageable getPageable() {
        Sort sort = Sort.by(new Sort.Order(getDirection(), getSortProperty()));
        return PageRequest.of(getPage(), getCount(), sort);
    }


}
