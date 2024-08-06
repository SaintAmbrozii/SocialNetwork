package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.Account;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account,Long> , JpaSpecificationExecutor<Account> {

     Optional<Account> findById(Long id);
}
