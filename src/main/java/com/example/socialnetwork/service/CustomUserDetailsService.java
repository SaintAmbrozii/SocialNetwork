package com.example.socialnetwork.service;


import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exception.NotFoundException;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return UserPrincipal.create(user);
    }
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new NotFoundException("Can't find user by ID"));

        return UserPrincipal.create(user);
    }
}
