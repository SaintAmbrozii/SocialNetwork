package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Account;
import com.example.socialnetwork.domain.UserContact;
import com.example.socialnetwork.domain.UserStatus;
import com.example.socialnetwork.domain.searchcriteria.ContactsSearchCriteria;
import com.example.socialnetwork.exception.NotFoundException;
import com.example.socialnetwork.repo.AccountRepo;
import com.example.socialnetwork.repo.UserContactRepo;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.repo.specifications.UserContactSpecs;
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




    public List<UserContact> createRequestFriends(Long toAccountId, UserPrincipal principal) {

        if (alreadySendRequests(toAccountId,principal).isEmpty()) {

            List<UserContact> userContactList = new ArrayList<>();
            UserContact toContact = UserContact.builder()
                    .fromAccountId(principal.getId())
                    .status(UserStatus.REQUEST_TO)
                    .toAccountId(toAccountId)
                    .created_at(LocalDateTime.now())
                    .build();
            userContactList.add(toContact);
            UserContact fromContact = UserContact.builder()
                    .fromAccountId(toAccountId)
                    .status(UserStatus.REQUEST_FROM)
                    .toAccountId(principal.getId())
                    .created_at(LocalDateTime.now())
                    .build();
            userContactList.add(fromContact);

            return contactRepo.saveAll(userContactList);

        }
        throw new  NotFoundException("данный пользователь отсуствует");

    }

    private List<UserContact> alreadySendRequests(Long toAccountId,UserPrincipal principal) {

        List<UserContact> contactList = contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(new ContactsSearchCriteria(principal.getId(),UserStatus.REQUEST_TO,toAccountId)));
        contactList.addAll(contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(new ContactsSearchCriteria(toAccountId,UserStatus.REQUEST_TO, principal.getId()))));
        contactList.addAll(contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(new ContactsSearchCriteria(principal.getId(),UserStatus.REQUEST_FROM,toAccountId))));
        contactList.addAll(contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(new ContactsSearchCriteria(toAccountId,UserStatus.REQUEST_FROM, principal.getId()))));
        return contactList;

    }









}
