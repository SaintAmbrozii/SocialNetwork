package com.example.socialnetwork.security.oauth;

import com.example.socialnetwork.domain.Token;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repo.TokenRepo;
import com.example.socialnetwork.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
public class LogoutService implements LogoutHandler {

    private final TokenRepo tokenRepository;
    private final AccountService accountService;

    public LogoutService(TokenRepo tokenRepository, @Lazy AccountService accountService) {
        this.tokenRepository = tokenRepository;
        this.accountService = accountService;
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            jwt = authHeader.substring(7);
            Optional<Token> userToken = tokenRepository.findByToken(jwt);
            if(userToken.isPresent()){
                Token existedToken = userToken.get();
                existedToken.setExpired(true);
                existedToken.setRevoked(true);
                tokenRepository.save(existedToken);

                Optional<User> user = tokenRepository.findUserById(existedToken.getId());
                if(user.isPresent()){
                    User accountUser = user.get();
                    accountService.setOnlineStatus(accountUser.getId(),false);
                }
            }
        }

    }
}
