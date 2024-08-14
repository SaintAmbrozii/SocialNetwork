package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Account;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.searchcriteria.AccountSearchCriteria;
import com.example.socialnetwork.dto.AccountDTO;
import com.example.socialnetwork.exception.NotFoundException;
import com.example.socialnetwork.repo.AccountRepo;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.repo.specifications.AccountSpesc;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {


    private final UserRepo userRepo;
    private final AccountRepo accountRepo;
    private final ContactService contactService;

    public AccountService(UserRepo userRepo, AccountRepo accountRepo, @Lazy ContactService contactService) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
        this.contactService = contactService;
    }

    public Page<AccountDTO> searchAccount(AccountSearchCriteria criteria, Pageable page,UserPrincipal principal) {

        List<Long> blockedByIds = contactService.getBlockedFriendsIds(principal.getId());
        blockedByIds.addAll(contactService.getBlockedUserFriendsIds(principal));
        if (blockedByIds.size() != 0) {
            criteria.setBlockedByIds(blockedByIds);
        }

        Page<Account> accountSearchPages;
        if (criteria.getAuthor() != null) {
            accountSearchPages = accountRepo.findAll(AccountSpesc.getSearchByAuthor(criteria), page);
        } else {
            accountSearchPages = accountRepo.findAll(AccountSpesc.getSearchByAllFields(criteria), page);
        }

        accountSearchPages.stream().forEach(ac->ac.setStatus(contactService.getStatus(ac.getId(), principal)));

        return accountSearchPages.map(AccountDTO::toDto);
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

    public Optional<AccountDTO> getMyAccount(UserPrincipal principal) {
       return  accountRepo.findById(principal.getId()).map(AccountDTO::toDto);

    }

    public Optional<AccountDTO> findById(Long id) {
        return accountRepo.findById(id).map(AccountDTO::toDto);
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
