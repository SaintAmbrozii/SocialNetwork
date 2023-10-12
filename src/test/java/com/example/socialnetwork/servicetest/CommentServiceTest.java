package com.example.socialnetwork.servicetest;

import com.example.socialnetwork.domain.Comment;
import com.example.socialnetwork.domain.Post;
import com.example.socialnetwork.domain.Role;
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
import java.util.Collections;

import static com.example.socialnetwork.Const.*;
import static com.example.socialnetwork.Const.EMAIL;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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

    public static User getDefaultUser() {
        var result = User.builder().id(ID).name(NAME).lastname(SECOND_NAME).
                password(PASSWORD).email(EMAIL).phone(PHONE).build();
        result.setRoles(Collections.singleton(Role.USER));

        return result;
    }

    public static Comment getDefaultComment() {
        Post post = Post.builder().id(commentId).comments(new ArrayList<>()).build();
        var result = new Comment();
        result.setId(1L);
        result.setPost(post);
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
    public void addCommentByPost() {}{
        Post post = getDefaultPost();
        Comment comment = Comment.builder().id(commentId).build();


        assertEquals(1,post.getComments().size());
    }


}
