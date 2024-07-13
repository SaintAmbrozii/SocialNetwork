package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exception.IgnoredSocialNetworkException;
import com.example.socialnetwork.exception.NotFoundException;
import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;
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


    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public List<UserDTO> getAllUsers () {
        return userRepo.findAll().stream().map(this::doDto).collect(Collectors.toList());
    }


    public Optional<User> findByEmail (String email) {
        return userRepo.findUserByEmail(email);
    }


    public Optional<User> findByUsername(String name) {
        return userRepo.findByName(name);
    }

    public User update(Long id, UserPrincipal userPrincipal) {
        User user = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        if (Objects.equals(id, userPrincipal.getId())) {
            userRepo.save(user);
        }
       return user;
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }
    public boolean isPhoneNumberTaken(String phoneNumber){
        return userRepo.findByPhone(phoneNumber).isPresent();
    }

    public boolean doesUsernameExists(String username){
        return userRepo.findUserByEmail(username).isPresent();
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
        dto.setLastname(user.getLastname());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        dto.setEnabled(user.getEnabled());
        dto.setPicture(user.getPicture());
        dto.setLocale(user.getLocale());
        dto.setAuthority(user.getAuthority());
        return dto;

    }
}
