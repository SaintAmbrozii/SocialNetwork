package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.exception.IgnoredSocialNetworkException;
import com.example.socialnetwork.payload.PostDTO;
import com.example.socialnetwork.payload.UserDTO;
import com.example.socialnetwork.repo.PostRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepo postRepo;
    private final UserDetailService userDetailService;
    private final UserService userService;
    private final FileService fileService;

;


    public PostService(PostRepo postRepo, UserDetailService userDetailService, UserService userService1,
                       FileService fileService) {
        this.postRepo = postRepo;
        this.userDetailService = userDetailService;
        this.userService = userService1;
        this.fileService = fileService;
    }
    public Post addPost(Principal principal,PostDTO postDTO) {
        Post post = new Post();
        post.setText(post.getText());
        return postRepo.save(post);
    }
    public Post postWithImg(String text,MultipartFile[] files) {
            Post post = new Post();
            post.setText(text);;
        if (files != null) {
            List<Image> images = Arrays.asList(files).stream()
                    .map(file -> {
                        String name = fileService.saveFile(file);
                        String uri = getFileData(file);
                        return new Image(name,uri);
                    }).collect(Collectors.toList());
            post.getImages().addAll(images);
        }
        return postRepo.save(post);
    }


    public void updPost(Long id, String text,MultipartFile[] files)  {
            Post inDB = postRepo.findById(id).orElseThrow();
            inDB.setText(text);
            inDB.getImages().removeAll(inDB.getImages());
            if (files!=null) {
                List<Image> images = Arrays.asList(files).stream()
                        .map(file -> {
                            String name = fileService.saveFile(file);
                            String uri = getFileData(file);
                            return new Image(name, uri);
                        }).collect(Collectors.toList());
                inDB.getImages().addAll(images);
            }
            postRepo.save(inDB);

    }
    public void deletePost(Post post) {
        postRepo.delete(post);
    }

    public List<PostDTO> getPosts() {
        return postRepo.findAll().stream().map(this::toPostDto).collect(Collectors.toList());
    }
    public Post getById(Long id) {
       return postRepo.findById(id).orElseThrow();
    }

    public Post react( Long id, User user, Reactions reactions) {
        Post inDB = postRepo.findById(id).orElseThrow();
        doReaction(inDB,user,reactions);
        checkIfAlreadyReactedOppositeAndIfRemove(reactions,user,inDB);
        userService.update(user);
        return postRepo.save(inDB);
    }

    private void doReaction(Post post, User reactedByUser, Reactions reaction) {
        switch (reaction) {
            case LIKE:
                if (reactedByUser.getLikesPosts().add(post))
                    post.plusLikes();
                else throwAlreadyReactedIgnored();
                break;
            case UNLIKE:
                if (reactedByUser.getLikesPosts().remove(post))
                    post.minusLikes();
                else throwNotReactedIgnored();
                break;
            case DISLIKE:
                if (reactedByUser.getDislikesPosts().add(post))
                    post.plusDislikes();
                else throwAlreadyReactedIgnored();
                break;
            case UN_DISLIKE:
                if (reactedByUser.getDislikesPosts().remove(post))
                    post.minusDislikes();
                else throwNotReactedIgnored();
                break;
        }
    }
    private void checkIfAlreadyReactedOppositeAndIfRemove(Reactions reaction, User user, Post post) {
        if (reaction == Reactions.LIKE)
            user.getDislikesPosts().remove(post);
        if (reaction == Reactions.DISLIKE)
            user.getLikesPosts().remove(post);
    }

    private void throwAlreadyReactedIgnored() {
        throw new IgnoredSocialNetworkException("Already reacted that way, ignored");
    }

    private void throwNotReactedIgnored() {
        throw new IgnoredSocialNetworkException("Not reacted that way, ignored");
    }


    private PostDTO toPostDto(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setUsername(String.valueOf(post.getAuthor()));
        postDTO.setId(post.getId());
        postDTO.setText(post.getText());
        postDTO.setImages(post.getImages());
        postDTO.setLikes(post.getLikes());
        postDTO.setDisLikes(post.getDisLikes());
        return postDTO;
    }

    private User getUser(UserDTO userDto) {
        return userService.findById(userDto.getId());
    }
    private String getFileData(MultipartFile file) {
        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(resultFilename)
                .toUriString();
        return uri;
    }







}
