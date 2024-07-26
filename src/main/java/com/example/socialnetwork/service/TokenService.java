package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Token;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repo.TokenRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

    private final TokenRepo tokenRepo;

    public TokenService(TokenRepo tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    public void saveToken(User user, String jwt){
        Token token = Token.builder()
                .token(jwt)
                .user(user)
                .build();

        tokenRepo.save(token);
    }

    public void revokeAllUserToken(User user){
        List<Token> allValidTokenByUser
                = tokenRepo.findAllValidTokenByUser(user.getId());

        if(allValidTokenByUser.isEmpty()) return;

        allValidTokenByUser.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepo.saveAll(allValidTokenByUser);
    }
}
