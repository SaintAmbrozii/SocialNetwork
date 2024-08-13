package com.example.socialnetwork.repo.specifications;

import com.example.socialnetwork.domain.Account;
import com.example.socialnetwork.domain.UserContact;
import com.example.socialnetwork.domain.searchcriteria.AccountSearchCriteria;
import com.example.socialnetwork.security.oauth.SecurityCheck;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountSpesc {

    public static Specification<Account> isNotOwnerUser(AccountSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty("id"))
                predicates.add(cb.equal(root.get("id"), SecurityCheck.getUserIdFromSecurityContext()).not());
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<Account> BasicId(AccountSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty("id"))
                predicates.add(cb.equal(root.get("id"), criteria.getId()));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<Account> getIds(AccountSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty("id"))
                predicates.add(cb.equal(root.get("id"), criteria.getIds()));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<Account> blockIds(AccountSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty("id"))
                predicates.add(cb.equal(root.get("id"), criteria.getBlockedByIds()).not());
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
    public static Specification<Account> getAuthorName(AccountSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty("name"))
                predicates.add(cb.like(root.get("name"), criteria.getAuthor()));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
    public static Specification<Account> getAuthorLastName(AccountSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty("lastname"))
                predicates.add(cb.like(root.get("lastName"), criteria.getAuthor()));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<Account> getName(AccountSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty("name"))
                predicates.add(cb.like(root.get("name"), criteria.getName().toLowerCase()));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
    public static Specification<Account> getLastName(AccountSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty("lastname"))
                predicates.add(cb.like(root.get("lastname"), criteria.getLastName().toLowerCase()));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<Account> getCity(AccountSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty("city"))
                predicates.add(cb.equal(root.get("city"), criteria.getCity()));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
    public static Specification<Account> getCountry(AccountSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty("country"))
                predicates.add(cb.equal(root.get("country"), criteria.getCountry()));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
    public static Specification<Account> getBirthDateSearch(AccountSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty("birth_date"))
                predicates.add(cb.between(root.get("birth_date"), criteria.getAgeTo() == null ? null : ZonedDateTime.now().minusYears(criteria.getAgeTo()),
                        criteria.getAgeFrom() == null ? null : ZonedDateTime.now().minusYears(criteria.getAgeFrom())));
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<Account> getSearchByAuthor(AccountSearchCriteria criteria) {
        return BasicId(criteria)
                .and(blockIds(criteria))
                .and(getAuthorName(criteria))
                .and(isNotOwnerUser(criteria))
                .and(getAuthorLastName(criteria))
                .and(isNotOwnerUser(criteria));
    }

    public static Specification<Account> getSearchByAllFields(AccountSearchCriteria criteria) {
        return BasicId(criteria)
                .and(blockIds(criteria))
                .and(getIds(criteria))
                .and(getName(criteria))
                .and(getLastName(criteria))
                .and(isNotOwnerUser(criteria))
                .and(getCountry(criteria))
                .and(getCity(criteria))
                .and(getBirthDateSearch(criteria));
    }

}
