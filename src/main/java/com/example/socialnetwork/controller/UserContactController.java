package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.searchcriteria.ContactsSearchCriteria;
import com.example.socialnetwork.dto.UserContactDTO;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import com.example.socialnetwork.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/contacts")
public class UserContactController {

    private final ContactService contactService;

    public UserContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public Page<UserContactDTO> getAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                       @RequestParam(value = "count", defaultValue = "100", required = false) int size,
                                       @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction,
                                       @RequestParam(value = "sort", defaultValue = "id", required = false) String sortProperty,
                                       @RequestParam(value = "search",required = false) ContactsSearchCriteria criteria,
                                       @AuthenticationPrincipal UserPrincipal principal) {

        Sort sort = Sort.by(new Sort.Order(direction, sortProperty));
        Pageable pageable = PageRequest.of(page, size, sort);

        return contactService.getAll(criteria,pageable,principal);

    }

    @GetMapping("{id}")
    public UserContactDTO findById(@PathVariable(name = "id") Long id){
        return contactService.findById(id);
    }

    @PutMapping("request/{id}")
    public List<UserContactDTO> addRequestFriends(@PathVariable(name = "id")Long id,@AuthenticationPrincipal UserPrincipal principal) {
        return contactService.createRequestFriends(id, principal);
    }
    
    @GetMapping("count")
    public Long countFriends(@AuthenticationPrincipal UserPrincipal principal) {
        return contactService.getCountRequestFriend(principal);
    }

    @PutMapping("{id}")
    public void addFriend(@PathVariable(name = "id")Long id, @AuthenticationPrincipal UserPrincipal principal) {
        contactService.approveFriend(id, principal);
    }

    @DeleteMapping("{id}")
    public void deleteFriend(@PathVariable(name = "id")Long id,@AuthenticationPrincipal UserPrincipal principal) {
        contactService.deleteFriendById(id, principal);
    }

    @PutMapping("block/{id}")
    public void blockUser(@PathVariable(name = "id")Long id,@AuthenticationPrincipal UserPrincipal principal) {
        contactService.blockFriend(id, principal);
    }
}
