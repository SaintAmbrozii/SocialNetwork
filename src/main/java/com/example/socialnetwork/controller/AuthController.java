package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.payload.LoginRequest;
import com.example.socialnetwork.payload.TokenRefreshRequest;
import com.example.socialnetwork.payload.TokenResponse;
import com.example.socialnetwork.payload.UserDTO;
import com.example.socialnetwork.service.AuthService;
import com.example.socialnetwork.service.UserService;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }
    @PostMapping(value = "login",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponse> authenticate(
            @RequestBody LoginRequest request
    ) throws AuthException {
        return ResponseEntity.ok(authService.authenticate(request));
    }
    @PostMapping("token")
    public ResponseEntity<TokenResponse> getNewToken(@RequestBody TokenRefreshRequest request) throws AuthException {
        final TokenResponse token = authService.getToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
    @PostMapping("refresh")
    public ResponseEntity<TokenResponse> getNewRefreshToken(@RequestBody TokenRefreshRequest request) throws AuthException {
        final TokenResponse token = authService.getRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

}
