package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PostRepo extends JpaRepository<Post,Long>, JpaSpecificationExecutor<Post> {

    List<Post> findPostByUserId(Long id);




}

