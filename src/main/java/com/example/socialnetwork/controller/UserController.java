package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;

import com.example.socialnetwork.payload.UserDTO;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import com.example.socialnetwork.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/users")
@SecurityRequirement(name = "JWTAuth")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @DeleteMapping("{id}")
   public void deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/profile")
    public User profile(@AuthenticationPrincipal UserPrincipal user) {
        return userService.findById(user.getId());
    }

    @Operation(description = "getAllUsers")
    @GetMapping()
    public List<UserDTO> allUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("{id}")
    public User updateUser(@PathVariable(name = "id")Long id, @RequestBody @AuthenticationPrincipal UserPrincipal principal) {;
        return userService.update(id, principal);
    }
    @GetMapping("{id}")
    public User findById(@PathVariable(name = "id")Long id) {
        return userService.findById(id);
    }

    @PutMapping("{id}/subscribes")
    public User subscribeByUser(@PathVariable(name = "id") Long id, @AuthenticationPrincipal  UserPrincipal userPrincipal){
        return userService.subscribeByUser(userPrincipal, id);
    }
    @PutMapping("{id}/unsubscribes")
    public User unSubscribeUser(@PathVariable(name = "id")Long id,@AuthenticationPrincipal UserPrincipal userPrincipal){
         return userService.unSubscribeByUser(userPrincipal, id);
    }
    @GetMapping("{id}/subscribers")
    public Set<User> subscribersByUser(@PathVariable(name = "id")Long id) {
        return userService.getAllSubscribers(id);
    }

    @GetMapping("{id}/subscriptions")
    public Set<User> subscriptionsByUser(@PathVariable(name = "id")Long id) {
        return userService.getAllSubscription(id);
    }
}
