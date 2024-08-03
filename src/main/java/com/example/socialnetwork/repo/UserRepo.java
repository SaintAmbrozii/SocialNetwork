package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.Account;
import com.example.socialnetwork.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {


    Optional<User> findUserByEmail(String email);


    Optional<User> findByName(String name);
    Optional<User> findByPhone(String number);





}
