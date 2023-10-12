package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepo extends JpaRepository<Post,Long> {

    List<Post> findPostByUserId(Long id);


}

