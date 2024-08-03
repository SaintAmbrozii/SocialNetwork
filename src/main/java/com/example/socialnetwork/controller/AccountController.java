package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Account;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import com.example.socialnetwork.service.AccountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Optional<Account> myAccount(@AuthenticationPrincipal UserPrincipal principal) {
        return accountService.getMyAccount(principal);
    }

    @PostMapping
    public Account create(@RequestBody Account account, @AuthenticationPrincipal UserPrincipal principal){
        return accountService.create(account,principal);
    }

    @PatchMapping("update")
    public Account update(@RequestBody Account account,@AuthenticationPrincipal UserPrincipal principal) {
        return accountService.update(account, principal);
    }

    @DeleteMapping
    public void delete(@AuthenticationPrincipal UserPrincipal principal) {
        accountService.delete(principal);
    }
}
