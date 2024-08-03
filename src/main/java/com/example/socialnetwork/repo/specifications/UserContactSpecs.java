package com.example.socialnetwork.repo.specifications;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.UserContact;
import com.example.socialnetwork.domain.UserStatus;
import com.example.socialnetwork.domain.searchcriteria.ContactsSearchCriteria;
import com.example.socialnetwork.domain.searchcriteria.UserSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;


public class UserContactSpecs {

    public static Specification<UserContact> BaseId(Long id) {
        return (root, criteriaQuery, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (!ObjectUtils.isEmpty(id))
                predicates.add(cb.equal(root.get("id"), id));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<UserContact> BaseId(ContactsSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty(criteria.getId()))
                predicates.add(cb.equal(root.get("id"), criteria.getId()));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<UserContact> getIds(ContactsSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty(criteria.getIds()))
                predicates.add(cb.equal(root.get("id"), criteria.getIds()));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<UserContact> getStatus(UserStatus status) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty(status))
                predicates.add(cb.equal(root.get("status"), status));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<UserContact> getFromId(ContactsSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty(criteria.getIdFrom()))
                predicates.add(cb.equal(root.get("from_acc_id"), criteria.getIdFrom()));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<UserContact> getToId(ContactsSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty(criteria.getIdTo()))
                predicates.add(cb.equal(root.get("to_acc_id"), criteria.getIdTo()));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }


    public static Specification <UserContact> getSeacrhContactSpecs(ContactsSearchCriteria criteria) {
        return BaseId(criteria)
                .and(getIds(criteria))
                .and(getStatus(criteria.getStatus()))
                .and(getFromId(criteria))
                .and(getToId(criteria));
    }
}
