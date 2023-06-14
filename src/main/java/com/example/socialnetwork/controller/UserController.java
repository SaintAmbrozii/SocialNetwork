package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.payload.UserDTO;
import com.example.socialnetwork.security.JwtTokenGenerator;
import com.example.socialnetwork.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;



    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user){
         return userService.createUser(user);
    }
    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable(name = "id")@AuthenticationPrincipal User user) {
        userService.deleteUser(user);
    }


    @GetMapping("/name")
    public Optional<User> getUserName(@RequestBody String name) {
        return userService.findByUsername(name);
    }
    @GetMapping
    public List<UserDTO> allUsers() {
        return userService.getAllUsers();
    }
    @PutMapping("{id}")
    public User updateUser(@PathVariable(name = "id")Long id,@RequestBody @AuthenticationPrincipal User user) {
        return userService.updateUser(id, user);
    }
    @GetMapping("{id}")
    public User findById(@PathVariable(name = "id")Long id) {
        return userService.findById(id);
    }
    @PutMapping("{id}/subscribes")
    public void subscribeByUser(@PathVariable(name = "id") Long id, @AuthenticationPrincipal User user){
        userService.subscribeByUser(user, id);
    }
    @PutMapping("{id}/unsubscribes")
    public void unSubscribeUser(@PathVariable(name = "id")Long id,@AuthenticationPrincipal User user){
         userService.unSubscribeByUser(user, id);
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
