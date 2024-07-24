package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.Token;
import com.example.socialnetwork.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token,Long> {

    @Query(
            """
            SELECT t FROM Token t WHERE t.user.id =:id
            AND (t.expired = false OR t.revoked = false)
            """
    )
    List<Token> findAllValidTokenByUser(Long id);

    @Query("SELECT t FROM Token t WHERE t.token=:token")
    Optional<Token> findByToken(String token);

    @Query("SELECT t.user FROM Token t WHERE t.id=:id")
    Optional<User> findUserById(Long id);

}
