package com.example.socialnetwork.repo;

import com.example.socialnetwork.domain.LikesPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepo extends JpaRepository<LikesPost,Long> {

    boolean existsLikesPostByUserId(Long id);

    int countLikesByPostId(Long PostId);

    List<LikesPost> findAllByUserId(Long userId);

    List<LikesPost> findAllByPostId(Long postId);

    LikesPost findByUserIdAndPostId(Long userId, Long postId);

}
