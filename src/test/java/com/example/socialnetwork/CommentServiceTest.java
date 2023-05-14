package com.example.socialnetwork;

import com.example.socialnetwork.domain.Comment;
import com.example.socialnetwork.domain.Post;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repo.CommentRepo;
import com.example.socialnetwork.repo.PostRepo;
import com.example.socialnetwork.service.CommentService;
import com.example.socialnetwork.service.PostService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import static com.example.socialnetwork.Const.PHONE;
import static org.junit.Assert.assertEquals;
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
    PostService postService;
    @InjectMocks
    CommentService commentService;


    private static final Long postId = 1L;

    private static final Long commentId = 1L;

    private static final Long userId = 1L;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

    }
    public static Comment getDefaultComment() {
        User expectedUser = User.builder()
                .id(userId)
                .name("user")
                .lastname("lastname")
                .email("user@test.com")
                .phone(PHONE)
                .build();
        Post post = Post.builder().id(commentId).comments(new ArrayList<>()).build();
        var result = new Comment();
        result.setId(1L);
        result.setAuthor(expectedUser);
        result.setPost(post);
        return result;
    }
    @Test
    public void addComment() {}{
        User user = UserServiceTest.getDefaultUser();
        Post post = Post.builder()
                .id(1L)
                .text("text")
                .author(user)
                .comments(new ArrayList<>()).build();
        Comment comment = Comment.builder().id(commentId).author(user).build();
        post.getComments().add(comment);
        assertEquals(1,post.getComments().size());
    }


}
