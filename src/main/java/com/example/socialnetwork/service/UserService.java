package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Role;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exception.IgnoredSocialNetworkException;
import com.example.socialnetwork.exception.UserIsExist;
import com.example.socialnetwork.payload.UserDTO;
import com.example.socialnetwork.repo.UserRepo;
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

    @Autowired
    private PasswordEncoder encoder;


    public User createUser(User user) {
        Optional <User> inDB = userRepo.findByEmail(user.getEmail());
        if (inDB.isPresent()) {
            throw new UserIsExist("пользователь с таким емайл уже есть");
        } user.setActive(true);
            user.setEmail(user.getEmail());
            user.setName(user.getName());
            user.setLastname(user.getLastname());
            user.setPhone(user.getPhone());
            user.setPassword(encoder.encode(user.getPassword()));
            user.setRoles(Collections.singleton(Role.USER));
         return  userRepo.save(user);
    }


    public void deleteUser(User user) {
        userRepo.delete(user);
    }

    public List<UserDTO> getAllUsers () {
        return userRepo.findAll().stream().map(this::doDto).collect(Collectors.toList());
    }

    public Optional<User> findByEmail (String email) {
        return userRepo.findByEmail(email);
    }



    public Optional<User> findByUsername(String name) {
        return userRepo.findByName(name);
    }

    public User update(User user) {
        return userRepo.save(user);
    }


    public User updateUser(Long id,User user) {
        User inDB = userRepo.findById(id).orElseThrow();
        if (inDB!=null) {
            inDB.setName(user.getName());
            inDB.setLastname(user.getLastname());
            inDB.setPassword(user.getPassword());
            inDB.setPhone(user.getPhone());
            inDB.setEmail(user.getEmail());
        }
        return userRepo.save(inDB);
    }
    public User findById(Long id){
        return userRepo.findById(id).orElseThrow();
    }

    public void subscribeByUser(User currentUser, Long id) {
        User subscribeByUser = userRepo.findById(id).orElseThrow();
        if (!currentUser.isSubscriptions(subscribeByUser)) {
            subscribeByUser.addSubscribers(currentUser);
            currentUser.addSubscriptions(subscribeByUser);
            userRepo.save(subscribeByUser);
            userRepo.save(currentUser);
        } else
            throw new IgnoredSocialNetworkException(String.format("Already following user id=%d, ignored", id));

    }
    public void unSubscribeByUser(User currentUser,Long id) {
        User subscribeUser = userRepo.findById(id).orElseThrow();
        if (currentUser.isSubscriptions(subscribeUser)) {
            currentUser.removeSubscriptions(subscribeUser);
            subscribeUser.removeSubscribers(currentUser);
            userRepo.save(subscribeUser);
            userRepo.save(currentUser);
        } else
            throw new IgnoredSocialNetworkException(String.format("Did not follow user id=%d, ignored", id));

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
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setAvatarUri(user.getAvatarUri());
        dto.setActive(user.isActive());
        dto.setRoles(user.getRoles());
        return dto;

    }
}
