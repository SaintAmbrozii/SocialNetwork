package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Comment;
import com.example.socialnetwork.domain.Post;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.payload.CommentDTO;
import com.example.socialnetwork.repo.CommentRepo;
import org.springframework.stereotype.Service;

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
        if (inDB!=null && currentUser.isActive()) {
            inDB.getComments().add(comment);
        }
        comment.setText(comment.getText());
        comment.setAuthor(currentUser);
        comment.setPost(inDB);
         return commentRepo.save(comment);
    }
    public void deleteComment(Comment comment,User currentUser) {
        if (currentUser.isActive()) {
            commentRepo.delete(comment);
        }
    }
    public Comment updComment(Long commentId, Comment comment,User currentUser) {
       Comment inDb = commentRepo.findById(commentId).orElseThrow();
       if (inDb!=null && currentUser.isActive()) {
           inDb.setText(comment.getText());
       }
       return commentRepo.save(inDb);
    }
    public Comment findById(Long id) {
        return commentRepo.findById(id).orElseThrow();
    }
    public List<CommentDTO> getAllComments () {
        return commentRepo.findAll().stream().map(this::toCommentDto).collect(Collectors.toList());
    }

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepo.getCommentByPostId(postId);
    }

    private CommentDTO toCommentDto(Comment comment) {
        CommentDTO commentDto = new CommentDTO();
        commentDto.setAuthor(String.valueOf(comment.getAuthor()));
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setPostId(commentDto.getPostId());
        return commentDto;
    }
}
