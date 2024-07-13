package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Comment;
import com.example.socialnetwork.dto.CommentDTO;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import com.example.socialnetwork.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
@SecurityRequirement(name = "JWTAuth")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping("posts/{id}/comments")
    public Comment create(@PathVariable(name = "id")Long postId, @RequestBody CommentDTO commentDTO, @AuthenticationPrincipal UserPrincipal user) {
        return commentService.addComment(postId, commentDTO,user);
    }
    @DeleteMapping("comments/{id}")
    public void delete(@PathVariable(name = "id") Comment comment,@AuthenticationPrincipal UserPrincipal user) {
        commentService.deleteComment(comment,user);
    }
    @PutMapping("comments/{id}")
    public Comment updComment( @PathVariable(name = "id")Long commentId, @RequestBody CommentDTO commentDTO,@AuthenticationPrincipal UserPrincipal user) {
        return commentService.updComment(commentId, commentDTO,user);
    }
    @GetMapping("posts/{id}/comments")
    public List<CommentDTO> getByPost(@PathVariable(name = "id")Long id) {
        return commentService.getCommentsByPost(id);
    }
    @GetMapping("comments")
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments();
    }
    @GetMapping("comments/{id}")
    public Comment findById(@PathVariable(name = "id") Long id){
        return commentService.findById(id);
    }
}
