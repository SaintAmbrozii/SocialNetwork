package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Account;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exception.NotFoundException;
import com.example.socialnetwork.repo.AccountRepo;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepo accountRepo;
    private final UserRepo userRepo;

    public AccountService(AccountRepo accountRepo, UserRepo userRepo) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
    }

    public Account create(Account account,UserPrincipal principal) {
        Optional<User> user = userRepo.findById(principal.getId());
        if (user.isPresent()) {
            User accountUser = user.get();
            Account newAccount = new Account();
            newAccount.setCreatedOn(ZonedDateTime.now());
            newAccount.setBirthDate(account.getBirthDate());
            newAccount.setRegDate(ZonedDateTime.now());
            newAccount.setOnline(true);
            newAccount.setAbout(account.getAbout());
            accountRepo.save(account);
        } throw new NotFoundException("пользователь отсуствует");
    }

    public void setOnlineStatus(Long id,boolean status) {
        Optional<Account> inDB = accountRepo.findByUserId(id);
        if (inDB.isPresent()) {
            Account account = inDB.get();
            account.setOnline(status);
            accountRepo.save(account);
        }
    }

}
