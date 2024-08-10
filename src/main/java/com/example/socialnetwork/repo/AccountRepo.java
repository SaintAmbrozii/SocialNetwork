package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.Account;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account,Long> , JpaSpecificationExecutor<Account> {

     @Query(value = "SELECT account.id FROM account WHERE extract(MONTH FROM account.birth_date) = :month AND extract(DAY FROM account.birth_date) = :day", nativeQuery = true)
     List<Long> getAccountsByBirthDateMonthAndDay(Integer month, Integer day);

     Optional<Account> findById(Long id);
}
