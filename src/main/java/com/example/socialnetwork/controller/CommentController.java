package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Comment;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.payload.CommentDTO;
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
    public Comment create(@PathVariable(name = "id")Long postId, @RequestBody Comment comment, @AuthenticationPrincipal User user) {
        return commentService.addComment(postId, comment,user);
    }
    @DeleteMapping("comments/{id}")
    public void delete(@PathVariable(name = "id") Comment comment,@AuthenticationPrincipal User user) {
        commentService.deleteComment(comment,user);
    }
    @PutMapping("comments/{id}")
    public Comment updComment( @PathVariable(name = "id")Long commentId, @RequestBody Comment comment,@AuthenticationPrincipal User user) {
        return commentService.updComment(commentId, comment,user);
    }
    @GetMapping("posts/{id}/comments")
    public List<Comment> getByPost(@PathVariable(name = "id")Long id) {
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
