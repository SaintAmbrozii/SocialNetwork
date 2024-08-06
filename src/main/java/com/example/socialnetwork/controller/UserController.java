package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;

import com.example.socialnetwork.domain.searchcriteria.UserSearchCriteria;
import com.example.socialnetwork.dto.UserDTO;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.repo.specifications.UserSpecs;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import com.example.socialnetwork.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/users")
@SecurityRequirement(name = "JWTAuth")
@Slf4j
public class UserController {


    private final UserService userService;
    private final UserRepo userRepo;

    public UserController(UserService userService, UserRepo userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }


    @GetMapping
    public Page<UserDTO> list(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                              @RequestParam(value = "count", defaultValue = "50", required = false) int size,
                              @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction,
                              @RequestParam(value = "sort", defaultValue = "id", required = false) String sortProperty) {
        Sort sort = Sort.by(new Sort.Order(direction, sortProperty));
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepo.findAll(pageable).map(UserDTO::toDto);
    }

    @GetMapping("/filter")
    public Page<UserDTO> filter(UserSearchCriteria query) {
        log.debug("UserSearchCriteria={}", query);


        return userRepo
                .findAll(UserSpecs.accordingToReportProperties(query), query.getPageable())
                .map(UserDTO::toDto);
    }


    @DeleteMapping("{id}")
   public void deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/profile")
    public User profile(@AuthenticationPrincipal UserPrincipal user) {
        return userService.findById(user.getId());
    }

 //   @Operation(description = "getAllUsers")
 //   @GetMapping()
 //   public List<UserDTO> allUsers() {
 //       return userService.getAllUsers();
 //   }

    @PutMapping("{id}")
    public User updateUser(@PathVariable(name = "id")Long id, @RequestBody @AuthenticationPrincipal UserPrincipal principal) {;
        return userService.update(id, principal);
    }
    @GetMapping("{id}")
    public User findById(@PathVariable(name = "id")Long id) {
        return userService.findById(id);
    }


}
