package com.example.socialnetwork.controller;


import com.example.socialnetwork.domain.Post;
import com.example.socialnetwork.domain.Reactions;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.payload.PostDTO;

import com.example.socialnetwork.security.oauth.UserPrincipal;
import com.example.socialnetwork.service.PostService;
import com.example.socialnetwork.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "JWTAuth")
public class PostController {

    private final PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;

    }


 //   @PostMapping(value = "posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 //   public Post addPost(@RequestPart(name = "text",required = false) PostDTO postDTO,
   //                     @RequestPart(name = "file",required = false) MultipartFile [] files,@AuthenticationPrincipal UserPrincipal user)  {
   //     return postService.postWithImg(postDTO,files,user);
  //  }
    @PostMapping(value = "posts")
    public Post createPost(@RequestBody PostDTO postDTO,@AuthenticationPrincipal UserPrincipal user) {
        return postService.addPost(user, postDTO);
    }
    @GetMapping("posts/getAllByUserId")
    public List<PostDTO> getAllByUser(@AuthenticationPrincipal UserPrincipal user) {
        return postService.getAllByUserId(user);
    }


    @PutMapping(value = "posts/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostDTO updMessage(@PathVariable(name = "id")Long id, @RequestPart(name = "text",required = false) PostDTO postDTO,
                           @RequestPart(name = "file",required = false)List<MultipartFile> files,@AuthenticationPrincipal UserPrincipal user) {
        return postService.updPost(id, postDTO, files,user);
    }
    @GetMapping(value = "posts")
    public List<PostDTO> getPosts() {
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
    public Post like(@PathVariable(name = "id") Long id, @AuthenticationPrincipal UserPrincipal user) {
        return postService.react(id, user, Reactions.LIKE);
    }
    @PutMapping("posts/{id}/dislikes")
    public Post disLike(@PathVariable(name = "id") Long id,@AuthenticationPrincipal UserPrincipal user) {
        return postService.react(id,user,Reactions.DISLIKE);
    }
    @PutMapping("posts/{id}/unlikes")
    public Post unLike(@PathVariable(name = "id") Long id,@AuthenticationPrincipal UserPrincipal user) {
        return postService.react(id,user,Reactions.UNLIKE);
    }
    @PutMapping("posts/{id}/undislikes")
    public Post unDislike(@PathVariable(name = "id") Long id,@AuthenticationPrincipal UserPrincipal user) {
        return postService.react(id,user,Reactions.UN_DISLIKE);
    }

}
