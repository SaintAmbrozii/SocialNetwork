package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Account;
import com.example.socialnetwork.domain.searchcriteria.AccountSearchCriteria;
import com.example.socialnetwork.domain.searchcriteria.ContactsSearchCriteria;
import com.example.socialnetwork.dto.AccountDTO;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import com.example.socialnetwork.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<AccountDTO> searchAccounts(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                           @RequestParam(value = "count", defaultValue = "50", required = false) int size,
                                           @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction,
                                           @RequestParam(value = "sort", defaultValue = "id", required = false) String sortProperty,
                                           @RequestParam(value = "search",required = false) AccountSearchCriteria criteria,
                                           @AuthenticationPrincipal UserPrincipal principal) {
        Sort sort = Sort.by(new Sort.Order(direction, sortProperty));
        Pageable pageable = PageRequest.of(page, size, sort);
        return accountService.searchAccount(criteria,pageable,principal);
    }

    @GetMapping("my")
    public Optional<AccountDTO> myAccount(@AuthenticationPrincipal UserPrincipal principal) {
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
