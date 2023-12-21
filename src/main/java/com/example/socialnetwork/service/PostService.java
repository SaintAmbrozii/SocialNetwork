package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.exception.IgnoredSocialNetworkException;
import com.example.socialnetwork.payload.PostDTO;
import com.example.socialnetwork.repo.ImageRepo;
import com.example.socialnetwork.repo.PostRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PostService {

    private final PostRepo postRepo;
    private final UserService userService;
    private final FileService fileService;
    private final ImageRepo imageRepo;







    public PostService(PostRepo postRepo, UserService userService,
                       FileService fileService, ImageRepo imageRepo) {
        this.postRepo = postRepo;
        this.userService = userService;
        this.fileService = fileService;
        this.imageRepo = imageRepo;

    }
    public Post addPost(User user,PostDTO postDTO) {
        Post post = new Post();
        post.setText(postDTO.getText());
        post.setUsername(user.getName());

        post.setUserId(user.getId());
        return postRepo.save(post);
    }
    @Transactional
    public Post postWithImg(PostDTO postDTO,MultipartFile [] files,User user) {
            Post post = new Post();
            post.setText(postDTO.getText());
            post.setUsername(user.getName());
            post.setUserId(user.getId());
            post.setDateTime(LocalDateTime.now());
        if (!(files == null)) {
            List<Image> images = Arrays.stream(files)
                    .map(file -> {
                        String name =  fileService.saveFile(file);
                        String uri = getFileData(file);
                        return imageRepo.save(Image.builder().uri(uri).name(name).build());
                    }).collect(Collectors.toList());

            post.getImages().addAll(images);
        }
        return postRepo.save(post);
    }


    @Transactional
    public PostDTO updPost(Long id, PostDTO postDTO, List<MultipartFile> files,User user)  {
            Post inDB = postRepo.findById(id).orElseThrow();
            if (user!=null) {
                inDB.setText(postDTO.getText());
                inDB.setDateTime(LocalDateTime.now());
                if (!files.isEmpty()) {
                    inDB.getImages().removeAll(inDB.getImages());
                    List<Image> images = Stream.of(files)
                            .map(file -> {
                                String name =  fileService.saveFile((MultipartFile) file);
                                String uri = getFileData((MultipartFile) file);
                                return imageRepo.save(Image.builder().uri(uri).name(name).build());
                            }).collect(Collectors.toList());

                    inDB.getImages().addAll(images);
                }

                postRepo.save(inDB);

            }
            return toPostDto(inDB);
    }
    @Transactional
    public void deletePost(Post post) {
        postRepo.delete(post);
    }

    public List<PostDTO> getPosts() {
        return postRepo.findAll().stream().map(this::toPostDto).collect(Collectors.toList());
    }
    public Post findById(Long id) {
       return postRepo.findById(id).orElseThrow();
    }

    public List<PostDTO> getAllByUserId(User user) {
        return postRepo.findPostByUserId(user.getId()).stream().map(this::toPostDto).collect(Collectors.toList());
    }

    @Transactional
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
        postDTO.setUsername(post.getUsername());
        postDTO.setLastname(post.getLastname());
        postDTO.setUserId(postDTO.getUserId());
        postDTO.setId(post.getId());
        postDTO.setText(post.getText());
        postDTO.setImages(post.getImages());
        postDTO.setLikes(post.getLikes());
        postDTO.setDisLikes(post.getDisLikes());
        return postDTO;
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
