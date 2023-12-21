package com.example.socialnetwork.controller;


import com.example.socialnetwork.domain.AuthProvider;
import com.example.socialnetwork.domain.Role;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.payload.ApiResponse;
import com.example.socialnetwork.payload.LoginRequest;
import com.example.socialnetwork.payload.SignUpRequest;
import com.example.socialnetwork.payload.TokenResponse;
import com.example.socialnetwork.security.oauth.TokenProvider;
import com.example.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder encoder;


    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);

        return ResponseEntity.ok(new TokenResponse(token));
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        Optional<User> userFromDB = userService.findByUsername(signUpRequest.getEmail());

        if (userFromDB.isPresent()) {
            throw new BadCredentialsException("Username is already exists");
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setName(signUpRequest.getName());
        user.setLastname(signUpRequest.getLastname());
        user.setPhone(signUpRequest.getPhone());
        user.setProvider(AuthProvider.LOCAL);
        user.setEnabled(1);
        user.setAuthority(Collections.singleton(Role.ROLE_USER));

        User userAfterSaving = userService.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(userAfterSaving.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }
}
