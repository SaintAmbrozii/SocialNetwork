package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Comment;
import com.example.socialnetwork.domain.Post;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.payload.CommentDTO;
import com.example.socialnetwork.repo.CommentRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final PostService postService;


    public CommentService(CommentRepo commentRepo, PostService postService) {
        this.commentRepo = commentRepo;
        this.postService = postService;
    }
    public Comment addComment(Long postId, Comment comment,User currentUser) {
        Post inDB = postService.findById(postId);
        if (currentUser.isEnabled()) {
            inDB.getComments().add(comment);
        }
        comment.setText(comment.getText());
        comment.setUserId(currentUser.getId());
        comment.setUsername(comment.getUsername());
        comment.setLastname(currentUser.getLastname());
        comment.setDateTime(LocalDateTime.now());
         return commentRepo.save(comment);
    }
    public void deleteComment(Comment comment,User currentUser) {
        if (currentUser.isEnabled()) {
            commentRepo.delete(comment);
        }
    }
    public Comment updComment(Long commentId, CommentDTO commentDTO,User currentUser) {
       Comment inDb = commentRepo.findById(commentId).orElseThrow();
       if (currentUser.isEnabled()) {
           inDb.setText(commentDTO.getText());
           inDb.setDateTime(LocalDateTime.now());
       }
       return commentRepo.save(inDb);
    }
    public Comment findById(Long id) {
        return commentRepo.findById(id).orElseThrow();
    }

    public List<CommentDTO> getAllComments () {
        return commentRepo.findAll().stream().map(this::toCommentDto).collect(Collectors.toList());
    }

    public List<CommentDTO> getCommentsByPost(Long postId) {
        return commentRepo.getCommentByPostId(postId).stream().map(this::toCommentDto).collect(Collectors.toList());
    }

    private CommentDTO toCommentDto(Comment comment) {
        CommentDTO commentDto = new CommentDTO();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setPostId(commentDto.getPostId());
        commentDto.setUsername(commentDto.getUsername());
        commentDto.setLastname(commentDto.getLastname());
        commentDto.setUserId(commentDto.getUserId());
        commentDto.setDateTime(comment.getDateTime());
        return commentDto;
    }
}
