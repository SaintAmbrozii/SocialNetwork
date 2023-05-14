package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exception.NotFoundSocialNetworkException;
import com.example.socialnetwork.repo.UserRepo;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    public UserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      User auth = userRepo.findByEmail(email).orElseThrow();
      return auth;


   //     return User.build(authUser);
        //org.springframework.security.core.userdetails.User.builder()
              // // .username(authUser.getName())
               // .password(authUser.getPassword())
              //  .authorities(authUser.getRoles())
              //  .build();
    }
}