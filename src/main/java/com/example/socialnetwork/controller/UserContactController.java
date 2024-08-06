package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.UserContactDTO;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import com.example.socialnetwork.service.ContactService;
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
