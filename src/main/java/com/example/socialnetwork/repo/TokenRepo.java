package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepo extends JpaRepository<Token,Long> {

}
