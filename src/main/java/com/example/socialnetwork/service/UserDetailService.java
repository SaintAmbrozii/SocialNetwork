package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Role;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exception.NotFoundSocialNetworkException;
import com.example.socialnetwork.repo.UserRepo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    public UserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = userRepo.findByEmail(email).orElseThrow();
        if (user==null) {
            throw new NotFoundSocialNetworkException("No user exists with this email.");
        } else {
            return user;
        }
      

    }



   //     return User.build(authUser);
        //org.springframework.security.core.userdetails.User.builder()
              // // .username(authUser.getName())
               // .password(authUser.getPassword())
              //  .authorities(authUser.getRoles())
              //  .build();
    }
