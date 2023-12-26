package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Comment;
import com.example.socialnetwork.domain.Post;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exception.NotFoundException;
import com.example.socialnetwork.payload.CommentDTO;
import com.example.socialnetwork.repo.CommentRepo;
import com.example.socialnetwork.repo.PostRepo;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.security.oauth.UserPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final UserRepo userRepo;


    public CommentService(CommentRepo commentRepo, PostRepo postRepo, UserRepo userRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }
    public Comment addComment(Long postId, CommentDTO commentDTO, UserPrincipal currentUser) {
        Optional<User> user = userRepo.findById(currentUser.getId());
        if (Objects.equals(currentUser.getId(), user.get().getId()))
        {
            throw new NotFoundException("нет данного пользователя");
        }
          Post post = postRepo.findById(postId).orElseThrow();
           Comment comment = Comment.builder().postId(post.getId())
                   .id(commentDTO.getId())
                   .text(commentDTO.getText())
                   .userId(currentUser.getId())
                   .username(commentDTO.getUsername())
                   .lastname(commentDTO.getLastname())
                   .dateTime(LocalDateTime.now()).build();
            return commentRepo.save(comment);
    }
    public void deleteComment(Comment comment,UserPrincipal currentUser) {
        Optional<User> user = userRepo.findById(currentUser.getId());
        if (Objects.equals(currentUser.getId(), user.get().getId()))
        {
            throw new NotFoundException("нет данного пользователя");
        }
          commentRepo.delete(comment);

    }
    public Comment updComment(Long commentId, CommentDTO commentDTO,UserPrincipal currentUser) {
        Optional<User> user = userRepo.findById(currentUser.getId());
        if (Objects.equals(currentUser.getId(), user.get().getId()))
        {
            throw new NotFoundException("нет данного пользователя");
        }Comment inDb = commentRepo.findById(commentId).orElseThrow();
        inDb.setText(commentDTO.getText());
        inDb.setDateTime(LocalDateTime.now());

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
