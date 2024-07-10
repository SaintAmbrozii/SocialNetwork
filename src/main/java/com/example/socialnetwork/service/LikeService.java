package com.example.socialnetwork.service;


import com.example.socialnetwork.domain.LikesPost;
import com.example.socialnetwork.domain.Post;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exception.UserIsExist;
import com.example.socialnetwork.repo.LikeRepo;
import com.example.socialnetwork.repo.PostRepo;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LikeService {

    private final PostRepo postRepo;
    private final LikeRepo likeRepo;
    private final UserRepo userRepo;

    public LikeService(PostRepo postRepo, LikeRepo likeRepo, UserRepo userRepo) {
        this.postRepo = postRepo;
        this.likeRepo = likeRepo;
        this.userRepo = userRepo;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public LikesPost addLikeToPost(Long postId, UserPrincipal principal) {
        Post inDB = postRepo.findById(postId).orElseThrow();
        User authUser = userRepo.findById(principal.getId()).orElseThrow();
        if (!likeRepo.existsLikesPostByUserId(authUser.getId())) {
            LikesPost newLike = LikesPost.builder()
                    .userId(authUser.getId())
                    .postId(inDB.getId())
                    .created_at(LocalDateTime.now()).build();
            inDB.plusDislikes();
            postRepo.save(inDB);
            return   likeRepo.save(newLike);
        }
        throw new UserIsExist("данный пост уже был лайкнут");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteLikeToPost(Long postId,UserPrincipal principal) {
        Post inDB = postRepo.findById(postId).orElseThrow();
        User authUser = userRepo.findById(principal.getId()).orElseThrow();
        LikesPost likes = likeRepo.findByUserIdAndPostId(inDB.getId(), authUser.getId());
        inDB.minusLikes();
        postRepo.save(inDB);
        likeRepo.delete(likes);
    }

    public int countLikePost(Long postId) {
        return likeRepo.countLikesByPostId(postId);
    }
}
