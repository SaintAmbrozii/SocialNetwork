package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Account;
import com.example.socialnetwork.domain.UserContact;
import com.example.socialnetwork.domain.UserStatus;
import com.example.socialnetwork.repo.AccountRepo;
import com.example.socialnetwork.repo.UserContactRepo;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {

    private final UserRepo userRepo;
    private final UserContactRepo contactRepo;
    private final AccountRepo accountRepo;

    public ContactService(UserRepo userRepo, UserContactRepo contactRepo, AccountRepo accountRepo) {
        this.userRepo = userRepo;
        this.contactRepo = contactRepo;
        this.accountRepo = accountRepo;
    }









}
