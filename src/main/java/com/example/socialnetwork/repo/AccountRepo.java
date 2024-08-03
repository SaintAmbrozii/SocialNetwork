package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.Account;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account,Long> {

     Optional<Account> findById(Long id);
}
