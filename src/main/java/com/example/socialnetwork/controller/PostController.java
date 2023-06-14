package com.example.socialnetwork.controller;


import com.example.socialnetwork.domain.Post;
import com.example.socialnetwork.domain.Reactions;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.payload.PostDTO;
import com.example.socialnetwork.security.JwtTokenGenerator;
import com.example.socialnetwork.service.PostService;
import com.example.socialnetwork.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final JwtTokenGenerator jwtTokenGenerator;

    public PostController(PostService postService, UserService userService, JwtTokenGenerator jwtTokenGenerator) {
        this.postService = postService;
        this.userService = userService;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }


    @PostMapping(value = "posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Post addPost(@RequestPart(name = "text",required = false)String text,
                        @RequestPart(name = "file",required = false) MultipartFile[] files)  {
        return postService.postWithImg(text,files);
    }
    @PutMapping(value = "posts/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updMessage(@PathVariable(name = "id")Long id, @RequestPart(name = "text",required = false)String text,
                           @RequestPart(name = "file",required = false)MultipartFile[] files) {
         postService.updPost(id, text, files);
    }
    @GetMapping(value = "posts")
    public List<PostDTO> getPosts(@RequestBody(required = false) PostDTO postDTO) {
        return postService.getPosts();
    }

    @GetMapping("posts/{id}")
    public Post findById(@PathVariable(name = "id") Long id){
        return postService.findById(id);
    }
    @DeleteMapping("posts/{id}")
    public void deletePost(@PathVariable(name = "id") Post post) {
        postService.deletePost(post);
    }

    @PutMapping("posts/{id}/likes")
    public Post like(@PathVariable(name = "id") Long id, @AuthenticationPrincipal User user) {
        return postService.react(id, user, Reactions.LIKE);
    }
    @PutMapping("posts/{id}/dislikes")
    public Post disLike(@PathVariable(name = "id") Long id,@AuthenticationPrincipal User user) {
        return postService.react(id,user,Reactions.DISLIKE);
    }
    @PutMapping("posts/{id}/unlikes")
    public Post unLike(@PathVariable(name = "id") Long id,@AuthenticationPrincipal User user) {
        return postService.react(id,user,Reactions.UNLIKE);
    }
    @PutMapping("posts/{id}/undislikes")
    public Post unDislike(@PathVariable(name = "id") Long id,@AuthenticationPrincipal User user) {
        return postService.react(id,user,Reactions.UN_DISLIKE);
    }

}
