package com.example.socialnetwork.repo.specifications;

import com.example.socialnetwork.domain.Post;

import com.example.socialnetwork.domain.searchcriteria.PostSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class PostSpecs {

    public static Specification<Post> accordingToReportProperties(PostSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (!ObjectUtils.isEmpty(criteria.getTitle()))
                predicates.add(cb.equal(root.get("title"), criteria.getTitle()));
            if (!ObjectUtils.isEmpty(criteria.getUsername()))
                predicates.add(cb.equal(root.get("username"), criteria.getUsername()));
            if (!ObjectUtils.isEmpty(criteria.getLastname()))
                predicates.add(cb.equal(root.get("lastname"), criteria.getLastname()));
            if (!ObjectUtils.isEmpty(criteria.getDateTime()))
                predicates.add(cb.equal(root.get("datetime"), criteria.getDateTime()));

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
