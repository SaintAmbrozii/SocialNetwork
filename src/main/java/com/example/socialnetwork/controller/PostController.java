package com.example.socialnetwork.controller;


import com.example.socialnetwork.domain.Post;
import com.example.socialnetwork.domain.Reactions;
import com.example.socialnetwork.domain.searchcriteria.PostSearchCriteria;
import com.example.socialnetwork.dto.PostDTO;

import com.example.socialnetwork.repo.PostRepo;
import com.example.socialnetwork.repo.specifications.PostSpecs;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import com.example.socialnetwork.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "JWTAuth")
@Slf4j
public class PostController {

    private final PostService postService;
    private final PostRepo postRepo;


    public PostController(PostService postService, PostRepo postRepo) {
        this.postService = postService;
        this.postRepo = postRepo;
    }

    @GetMapping
    public Page<PostDTO> list(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                              @RequestParam(value = "count", defaultValue = "10", required = false) int size,
                              @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction,
                              @RequestParam(value = "sort", defaultValue = "id", required = false) String sortProperty) {
        Sort sort = Sort.by(new Sort.Order(direction, sortProperty));
        Pageable pageable = PageRequest.of(page, size, sort);
        return postRepo.findAll(pageable).map(PostDTO::toDto);
    }

    @GetMapping("/filter")
    public Page<PostDTO> filter(PostSearchCriteria query) {
        log.debug("PostSearchCriteria={}", query);

        return postRepo.findAll(PostSpecs.accordingToReportProperties(query), query.getPageable())
                .map(PostDTO::toDto);
    }


    @PostMapping(value = "posts")
    public Post addPost(@RequestPart(name = "text",required = false) String text,
                        @RequestPart(name = "file",required = false) MultipartFile [] files,@AuthenticationPrincipal UserPrincipal user)  {
        return postService.postWithImg(text,files,user);
    }
  //  @PostMapping(value = "posts")
  //  public Post createPost(@RequestBody PostDTO postDTO,@AuthenticationPrincipal UserPrincipal user) {
  //      return postService.addPost(user, postDTO);
  //  }

    @GetMapping("posts/getAllByUserId")
    public List<PostDTO> getAllByUser(@AuthenticationPrincipal UserPrincipal user) {
        return postService.getAllByUserId(user);
    }


    @PutMapping(value = "posts/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostDTO updMessage(@PathVariable(name = "id")Long id, @RequestPart(name = "text",required = false) String text,
                           @RequestPart(name = "file",required = false)MultipartFile[] files,@AuthenticationPrincipal UserPrincipal user) {
        return postService.updPost(id, text, files,user);
    }

  //  @GetMapping(value = "posts")
  //  public List<PostDTO> getPosts() {
   //     return postService.getPosts();
   // }

    @GetMapping("posts/{id}")
    public PostDTO findById(@PathVariable(name = "id") Long id){
        return postService.findById(id);
    }

    @DeleteMapping("posts/{id}")
    public void deletePost(@PathVariable(name = "id") Post post) {
        postService.deletePost(post);
    }

    @PatchMapping("posts/{id}/like")
    public PostDTO likeByPost(@PathVariable(name = "id")Long id,@AuthenticationPrincipal UserPrincipal user) {
        return postService.likeByPost(id, user);
    }
    @PatchMapping("posts/{id}/unlike")
    public PostDTO deleteLikeByPost(@PathVariable(name = "id")Long id,@AuthenticationPrincipal UserPrincipal user) {
        return postService.deleteLikeByPost(id, user);
    }


}
