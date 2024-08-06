package com.example.socialnetwork.service;
import com.example.socialnetwork.domain.UserContact;
import com.example.socialnetwork.domain.UserStatus;
import com.example.socialnetwork.domain.searchcriteria.ContactsSearchCriteria;
import com.example.socialnetwork.dto.UserContactDTO;
import com.example.socialnetwork.exception.NotFoundException;
import com.example.socialnetwork.mapper.UserContactMapper;
import com.example.socialnetwork.repo.UserContactRepo;
import com.example.socialnetwork.repo.specifications.UserContactSpecs;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {


    private final UserContactRepo contactRepo;
    private final UserContactMapper userContactMapper;


    public ContactService(UserContactRepo contactRepo, UserContactMapper userContactMapper) {
        this.contactRepo = contactRepo;
        this.userContactMapper = userContactMapper;
    }

    public UserContactDTO findById(Long id) {

        return userContactMapper.convertToDto(contactRepo.findById(id).orElseThrow());

    }



    public List<Long> getMyFriendsIds(UserPrincipal principal) {

        ContactsSearchCriteria criteria = new ContactsSearchCriteria();
        criteria.setStatus(UserStatus.FRIEND);
        criteria.setIdFrom(principal.getId());
        return contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(criteria)).stream().map(UserContact::getToAccountId).collect(Collectors.toList());

    }

    public List<Long> getFriendsIds(Long id,UserPrincipal principal) {

        return contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(new ContactsSearchCriteria(id,UserStatus.FRIEND)))
                .stream()
                .map(UserContact::getToAccountId)
                .collect(Collectors.toList());

    }



    public void approveFriend(Long id, UserPrincipal principal) {

        List<UserContact> friendList = getCurrentInvoicesByAccountId(id,principal);
        if (!friendList.isEmpty()) {
            friendList.forEach(friend -> {
                if (friend.getStatus().equals(UserStatus.REQUEST_FROM)) {
                    friend.setStatus(UserStatus.FRIEND);
                    contactRepo.save(friend);
                }

            });
        }
    }

    public void deleteFriendById(Long id,UserPrincipal principal) {

        if (!IfUserBlocked(id,principal)) {
            List<UserContact> friendList = getCurrentInvoicesByAccountId(id,principal);
            friendList.forEach(friend -> {
                friend.setStatus(UserStatus.NONE);
                contactRepo.delete(friend);
            });

        } else {
            contactRepo.deleteAll(getRequestTo(id,principal));
        }
    }

    public Long getCountRequestFriend(UserPrincipal principal) {

        return (long) contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(new ContactsSearchCriteria(principal.getId(),UserStatus.REQUEST_FROM))).size();

    }


    public List<UserContactDTO> createRequestFriends(Long id, UserPrincipal principal) {


        if (alreadySendRequests(id,principal).isEmpty()) {

            List<UserContact> userContactList = new ArrayList<>();
            UserContact toContact = UserContact.builder()
                    .fromAccountId(principal.getId())
                    .status(UserStatus.REQUEST_TO)
                    .toAccountId(id)
                    .created_at(LocalDateTime.now())
                    .build();
            userContactList.add(toContact);
            UserContact fromContact = UserContact.builder()
                    .fromAccountId(id)
                    .status(UserStatus.REQUEST_FROM)
                    .toAccountId(principal.getId())
                    .created_at(LocalDateTime.now())
                    .build();
            userContactList.add(fromContact);

            contactRepo.saveAll(userContactList);

            return userContactMapper.convertToDtoList(userContactList);

        }
        throw new  NotFoundException("данный пользователь отсуствует");
    }

    public void blockFriend(Long id,UserPrincipal principal) {

        List<UserContact> requestUserFromFriends = getRequestFrom(id,principal);
        List<UserContact> requestToWithUser = getRequestTo(id,principal);

        if (IfUserBlocked(id,principal)) {
            blockIfMyContactBlocked(id,principal);
        } else if (requestUserFromFriends.isEmpty() && requestToWithUser.isEmpty()) {
            blockIfNoConnection(id,principal);
        } else {

            requestUserFromFriends.forEach(f -> {
                if (f.getStatus() != UserStatus.BLOCKED) {
                    contactRepo.deleteAll(requestUserFromFriends);
                    contactRepo.deleteAll(requestToWithUser);
                    blockIfNoConnection(id,principal);
                } else {
                    contactRepo.deleteAll(requestUserFromFriends);
                    contactRepo.deleteAll(requestToWithUser);
                }
            });
        }
    }


    private List<UserContact> alreadySendRequests(Long toAccountId,UserPrincipal principal) {

        List<UserContact> contactList = contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(new ContactsSearchCriteria(principal.getId(),UserStatus.REQUEST_TO,toAccountId)));
        contactList.addAll(contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(new ContactsSearchCriteria(toAccountId,UserStatus.REQUEST_TO, principal.getId()))));
        contactList.addAll(contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(new ContactsSearchCriteria(principal.getId(),UserStatus.REQUEST_FROM,toAccountId))));
        contactList.addAll(contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(new ContactsSearchCriteria(toAccountId,UserStatus.REQUEST_FROM, principal.getId()))));
        return contactList;

    }

    private List<UserContact> getCurrentInvoicesByAccountId(Long id, UserPrincipal principal) {


        List<UserContact> list = getRequestFrom(id,principal);
        list.addAll(getRequestTo(id,principal));
        return list;

    }

    private List<UserContact> getRequestFrom(Long id,UserPrincipal principal) {


        ContactsSearchCriteria criteria = new ContactsSearchCriteria();
        criteria.setIdFrom(principal.getId());
        criteria.setIdTo(id);
        return contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(criteria));

    }

    private List<UserContact> getRequestTo(Long id,UserPrincipal principal) {

        ContactsSearchCriteria criteria = new ContactsSearchCriteria();
        criteria.setIdFrom(id);
        criteria.setIdTo(principal.getId());
        return contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(criteria));

    }

    private Boolean IfUserBlocked(Long id,UserPrincipal principal) {

        return !contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(new ContactsSearchCriteria(id,UserStatus.BLOCKED, principal.getId()))).isEmpty();

    }
    private void blockIfMyContactBlocked(Long id,UserPrincipal principal) {

        if (getRequestFrom(id,principal).isEmpty()) {
            contactRepo.save(new UserContact(principal.getId(), UserStatus.BLOCKED, id));
        } else if (getRequestFrom(id,principal).get(0).getStatus().equals(UserStatus.BLOCKED)) {
            contactRepo.deleteAll(getRequestTo(id,principal));
        }
    }

    private void blockIfNoConnection(Long id,UserPrincipal principal) {

        contactRepo.save(new UserContact(principal.getId(), UserStatus.BLOCKED, id));

    }

    private boolean checkIfAccountSearch(ContactsSearchCriteria criteria) {

        return (criteria.getFirstName() != null || criteria.getAgeFrom() != null ||
                criteria.getAgeTo() != null || criteria.getCity() != null || criteria.getCountry() != null);

    }

    public List<Long> getBlockedUserFriendsIds(UserPrincipal principal) {

        return contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(new ContactsSearchCriteria(principal.getId(),UserStatus.BLOCKED)))
                .stream()
                .map(UserContact::getToAccountId)
                .collect(Collectors.toList());
    }
    public List<Long> getBlockedFriendsIds(Long id) {

        return contactRepo.findAll(UserContactSpecs.getSeacrhContactSpecs(new ContactsSearchCriteria(id,UserStatus.BLOCKED)))
                .stream()
                .map(UserContact::getToAccountId)
                .collect(Collectors.toList());
    }









}
