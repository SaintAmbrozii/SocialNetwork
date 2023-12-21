package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Role;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exception.IgnoredSocialNetworkException;
import com.example.socialnetwork.exception.NotFoundException;
import com.example.socialnetwork.exception.UserIsExist;
import com.example.socialnetwork.payload.UserDTO;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;




    public User save(User user) {
        return userRepo.save(user);
    }


    public void deleteUser(User user) {
        userRepo.delete(user);
    }

 //   public List<UserDTO> getAllUsers () {
 //       return userRepo.findAll().stream().map(this::doDto).collect(Collectors.toList());
 //   }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public Optional<User> findByEmail (String email) {
        return userRepo.findUserByEmail(email);
    }



    public Optional<User> findByUsername(String name) {
        return userRepo.findByName(name);
    }

    public User update(User user) {
        return userRepo.save(user);
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User subscribeByUser(UserPrincipal principal, Long id) {
        User subscribeByUser = userRepo.findById(id).orElseThrow();
        User currentUser = userRepo.findUserByEmail(principal.getUsername()).orElseThrow();
        if (!currentUser.isSubscriptions(subscribeByUser)) {
            subscribeByUser.addSubscribers(currentUser);
            currentUser.addSubscriptions(subscribeByUser);
            userRepo.save(subscribeByUser);
            userRepo.save(currentUser);
        } else
            throw new IgnoredSocialNetworkException(String.format("Already following user id=%d, ignored", id));
        return subscribeByUser;

    }
    public User unSubscribeByUser(UserPrincipal principal, Long id) {
        User subscribeUser = userRepo.findById(id).orElseThrow();
        User currentUser = userRepo.findUserByEmail(principal.getUsername()).orElseThrow();
        if (currentUser.isSubscriptions(subscribeUser)) {
            currentUser.removeSubscriptions(subscribeUser);
            subscribeUser.removeSubscribers(currentUser);
            userRepo.save(subscribeUser);
            userRepo.save(currentUser);
        } else
            throw new IgnoredSocialNetworkException(String.format("Did not follow user id=%d, ignored", id));
        return subscribeUser;

    }
    public Set<User> getAllSubscribers(Long id) {
        User inDB = userRepo.findById(id).orElseThrow();
        return   userRepo.findBySubscribers(inDB);

    }
    public Set<User> getAllSubscription(Long id){
        User inDB = userRepo.findById(id).orElseThrow();
        return userRepo.findBySubscriptions(inDB);
    }
    public UserDTO doDto (User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        return dto;

    }
}
