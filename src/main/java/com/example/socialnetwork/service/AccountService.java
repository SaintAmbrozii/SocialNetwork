package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Account;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exception.NotFoundException;
import com.example.socialnetwork.repo.AccountRepo;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class AccountService {


    private final UserRepo userRepo;
    private final AccountRepo accountRepo;

    public AccountService(UserRepo userRepo, AccountRepo accountRepo) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
    }

    public Account create(Account account,UserPrincipal principal) {
        Optional<User> user = userRepo.findById(principal.getId());
        if (user.isPresent()) {
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
        Optional<Account> inDB = accountRepo.findById(id);
        if (inDB.isPresent()) {
            Account account = inDB.get();
            account.setOnline(status);
            account.setLastOnlineTime(null);
            accountRepo.save(account);
        }
    }

    public Optional<Account> getMyAccount(UserPrincipal principal) {
       return  accountRepo.findById(principal.getId());

    }


    public Account update(Account account, UserPrincipal principal) {
        Optional<Account> forUser = accountRepo.findById(principal.getId());
        if (forUser.isPresent()){
            Account userAccount = forUser.get();
            userAccount.setAddress(account.getAddress());
            userAccount.setCity(account.getCity());
            userAccount.setCountry(account.getCountry());
            userAccount.setBirthDate(account.getBirthDate());
            userAccount.setAbout(account.getAbout());
            userAccount.setUpdatedOn(ZonedDateTime.now());
            userRepo.save(account);
        }
        throw new NotFoundException("пользователь отсуствует");

    }

    public void delete(UserPrincipal principal) {
        accountRepo.deleteById(principal.getId());
    }

}
