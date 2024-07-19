package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.exception.IgnoredSocialNetworkException;
import com.example.socialnetwork.exception.NotFoundException;
import com.example.socialnetwork.dto.PostDTO;
import com.example.socialnetwork.repo.ImageRepo;
import com.example.socialnetwork.repo.PostRepo;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepo postRepo;
    private final UserService userService;
    private final FileService fileService;
    private final ImageRepo imageRepo;
    private final UserRepo userRepo;







    public PostService(PostRepo postRepo, UserService userService,
                       FileService fileService, ImageRepo imageRepo, UserRepo userRepo) {
        this.postRepo = postRepo;
        this.userService = userService;
        this.fileService = fileService;
        this.imageRepo = imageRepo;
        this.userRepo = userRepo;
    }
    public Post addPost(UserPrincipal userPrincipal,PostDTO postDTO) {
        User user = userService.findById(userPrincipal.getId());
        if (Objects.equals(user.getId(), userPrincipal.getId())) {
            Post post = new Post();
            post.setText(postDTO.getText());
            post.setUsername(user.getName());
            post.setLastname(user.getLastname());
            post.setDateTime(LocalDateTime.now());
            post.setUserId(user.getId());
            return postRepo.save(post);

        } else throw new NotFoundException("user not found");

    }
    @Transactional
    public Post postWithImg(String text, MultipartFile [] files,UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getId());
        if (Objects.equals(user.getId(), userPrincipal.getId())) {
            Post post = new Post();
            post.setText(text);
            post.setUsername(user.getName());
            post.setLastname(user.getLastname());
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
        else throw new NotFoundException("user not found");


    }


    @Transactional
    public PostDTO updPost(Long id, String text, MultipartFile [] files, UserPrincipal userPrincipal)  {
            User user = userService.findById(userPrincipal.getId());
            Post inDB = postRepo.findById(id).orElseThrow();
            if (Objects.equals(user.getId(), userPrincipal.getId())) {
                inDB.setText(text);
                inDB.setDateTime(LocalDateTime.now());
                if (!(files == null)) {
                    inDB.getImages().removeAll(inDB.getImages());
                    List<Image> images = Arrays.stream(files)
                            .map(file -> {
                                String name =  fileService.saveFile(file);
                                String uri = getFileData(file);
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

    public List<PostDTO> getAllByUserId(UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getId());
        if (Objects.equals(user.getId(), userPrincipal.getId())) {
           return postRepo.findPostByUserId(user.getId()).stream().map(this::toPostDto).collect(Collectors.toList());
        } else throw new UsernameNotFoundException("user not found");
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
