package com.example.socialnetwork.servicetest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.socialnetwork.domain.Comment;
import com.example.socialnetwork.domain.Post;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exception.NotFoundSocialNetworkException;
import com.example.socialnetwork.dto.CommentDTO;
import com.example.socialnetwork.repo.CommentRepo;
import com.example.socialnetwork.repo.PostRepo;
import com.example.socialnetwork.service.CommentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static com.example.socialnetwork.Const.*;
import static com.example.socialnetwork.Const.EMAIL;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
public class CommentServiceTest {

    @Mock
    CommentRepo commentRepo;
    @Mock
    PostRepo postRepo;

    @InjectMocks
    CommentService commentService;


    private static final Long postId = 1L;

    private static final Long commentId = 1L;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

    }

    public static User getDefaultUser() {
        var result = User.builder().id(ID).name(NAME).lastname(SECOND_NAME).
               password(PASSWORD).email(EMAIL).phone(PHONE).build();
    //    result.setRoles(Collections.singleton(Role.USER));

       return result;
    }

    public static Comment getDefaultComment() {
        var result = new Comment();
        result.setId(1L);
        result.setUsername(getDefaultUser().getName());
        result.setUserId(getDefaultUser().getId());
        return result;
    }

    public static Post getDefaultPost() {
        var result = new Post();
        result.setId(ID);
        result.setText("text");
        result.setUsername(getDefaultUser().getName());
        result.setUserId(getDefaultUser().getId());

        return result;
    }

    @Test
    public void addCommentByPost() {


        when(postRepo.findById(postId)).thenReturn(Optional.of(getDefaultPost()));

        CommentDTO commentDTO = CommentDTO.builder()
                .postId(postId)
                .id(commentId)
                .userId(getDefaultUser().getId())
                .build();


     //   Comment returned = commentService.addComment(postId, commentDTO, getDefaultUser());
     //   when(commentRepo.save(returned))
   //             .thenReturn(returned);

    //    assertThat(returned).usingRecursiveComparison().isEqualTo(commentDTO);

        verify(postRepo, times(1)).findById(postId);
        verify(commentRepo, times(1)).save(any(Comment.class));
        verifyNoMoreInteractions(commentRepo);


    }

    @Test
    public void CommentDTOUpdateComment() {
        CommentDTO commentDTO = CommentDTO.builder()
                .text("Слава России")
                .build();
        Comment comment = Comment.builder()
                .id(commentId)
                .text("Слава")
                .build();
        Comment updatedComment = Comment.builder()
                .id(commentId)
                .text("Слава России")
                .build();

        given(commentRepo.findById(commentId))
                .willReturn(Optional.ofNullable(comment));
        given(commentRepo.save(updatedComment))
                .willReturn(updatedComment);

     //   Comment returnedComment = commentService.updComment(commentId, commentDTO, getDefaultUser());
   //     assertThat(returnedComment.getId()).isEqualTo(commentId);
   //     assertThat(returnedComment.getText()).isEqualTo("Слава России");

    }

    @Test
    public void getAllComents() {
        List<Comment> commentList = List.of(
                Comment.builder().id(1L).build(),
                Comment.builder().id(2L).build()
        );

        given(commentRepo.findAll())
                .willReturn(commentList);

        List<CommentDTO> returnedComments = commentService.getAllComments();
        assertThat(returnedComments).hasSize(2);
        assertThat(returnedComments.get(0).getId()).isEqualTo(1L);
        assertThat(returnedComments.get(1).getId()).isEqualTo(2L);
    }

    @Test
    public void findCommentsByPost()  {
        List<Comment> commentList = List.of(
                Comment.builder().id(1L).build(),
                Comment.builder().id(2L).build()
        );

        given(postRepo.existsById(1L))
                .willReturn(true);
        given(commentRepo.getCommentByPostId(1L))
                .willReturn(commentList);

        List<CommentDTO> returnedComments = commentService.getCommentsByPost(1L);
        assertThat(returnedComments).hasSize(2);
        assertThat(returnedComments.get(0).getId()).isEqualTo(1L);
        assertThat(returnedComments.get(1).getId()).isEqualTo(2L);
    }


    @Test
    public void testUpdCommentThrowsCommentResourceNotFoundException() {

        when(postRepo.findById(postId)).thenReturn(Optional.of(getDefaultPost()));

        CommentDTO commentDTO = CommentDTO.builder()
                .postId(postId)
                .id(commentId)
                .userId(getDefaultUser().getId())
                .build();


        given(commentRepo.findById(commentDTO.getId())).willAnswer(invocation -> {
            throw new NotFoundSocialNetworkException("post with id " + commentId + " does not exists");
        });

        Assert.assertThrows(NotFoundSocialNetworkException.class, () -> {
     //       commentService.updComment(commentId, commentDTO, getDefaultUser());
        });

    }
}
