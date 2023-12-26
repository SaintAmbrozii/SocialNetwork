package com.example.socialnetwork.servicetest;

import com.example.socialnetwork.Const;
import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.exception.NotFoundSocialNetworkException;
import com.example.socialnetwork.payload.PostDTO;
import com.example.socialnetwork.repo.PostRepo;
import com.example.socialnetwork.service.FileService;
import com.example.socialnetwork.service.PostService;
import com.example.socialnetwork.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

import static com.example.socialnetwork.Const.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PostServiceTest {

    @Mock
    PostRepo postRepository;

    @Mock
    UserService userService;

    @InjectMocks
    PostService postService;

    private static final Long userId = 1L;
    private static final Long postId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }
    public static User getDefaultUser() {
        var result = User.builder().id(ID).name(NAME).lastname(SECOND_NAME).
                password(PASSWORD).email(EMAIL).phone(PHONE).build();
        //   result.setRoles(Collections.singleton(Role.USER));

        return result;
    }



//
    @Test
    public void shouldFindAndReturnOnePost() {

        Post expectedPost = Post.builder().id(postId)
                .userId(userId)
                .images(new ArrayList<>()).
                build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(expectedPost));

        Post actualPost = postService.findById(postId);

        assertThat(actualPost).usingRecursiveComparison().isEqualTo(expectedPost);
        verify(postRepository, times(1)).findById(postId);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    public void testCreatePost() {

        Post post = Post.builder().id(postId)
                .userId(userId)
                .images(new ArrayList<>()).
                build();


        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostDTO postDTO = PostDTO.builder()
                .userId(userId)
                .images(new ArrayList<>())
                .build();

   //     Long actualId = postService.addPost(getDefaultUser(),postDTO).getId();

    //    assertEquals(postId, actualId);
        verify(postRepository, times(1)).save(any(Post.class));
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    public void testGetAllPost() {
        BDDMockito.given(postRepository.findAll()).willReturn(List.of(new Post(), new Post(), new Post()));

        assertThat(postService.getPosts()).hasSize(3);
        verify(postRepository, times(1)).findAll();
    }

    @Test
    public void testCreatePostWithImages()  {

        Post post = Post.builder().id(postId)
                .userId(getDefaultUser().getId())
                .images(new ArrayList<>()).
                build();

        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostDTO postDTO = PostDTO.builder()
                .userId(getDefaultUser().getId())
                .images(new ArrayList<>())
                .build();

      //  Long actualId = postService.postWithImg(postDTO,any(),getDefaultUser()).getId();

    //    assertEquals(postId, actualId);
        verify(postRepository, times(1)).save(any(Post.class));
        verifyNoMoreInteractions(postRepository);
    }
    @Test
    public void testUpdatePost()  {
        Post foundPost = Post.builder().id(postId)
                .userId(getDefaultUser().getId())
                .images(new ArrayList<>()).
                build();

        PostDTO expectedPostDTO = PostDTO.builder()
                .id(postId)
                .userId(getDefaultUser().getId())
                .images(new ArrayList<>())
                .build();


        when(postRepository.findById(postId)).thenReturn(Optional.of(foundPost));

     //   PostDTO actualPost = postService.updPost(postId,expectedPostDTO, anyList(), getDefaultUser());

   //     assertThat(actualPost).usingRecursiveComparison().isEqualTo(expectedPostDTO);
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(foundPost);
        verifyNoMoreInteractions(postRepository);
    }



    public static Post getDefaultPost() {
        var result = new Post();
        result.setId(ID);
        result.setText("text");
        for (int i = 0; i < LIKES; i++)
            result.plusDislikes();
        for (int i = 0; i < DISLIKES; i++)
            result.minusDislikes();



        return result;
    }

    @Test
    public void incrementLikes() throws IOException {
        var post = getDefaultPost();
        post.plusLikes();
        assertEquals(1, post.getLikes().intValue());
    }

    @Test
    public void incrementDislikes() throws IOException {
        var post = getDefaultPost();
        post.plusDislikes();
        assertEquals(1, post.getDisLikes().intValue());
    }

    @Test
    public void testDeletePost() {
        var user = getDefaultUser();
        Post post = Post.builder()
                .id(postId)
                .userId(getDefaultUser().getId())
                .text("text")
                .images(new ArrayList<>()).build();



        assertEquals(user.getId(),post.getUserId());
        postRepository.delete(post);
        verify(postRepository, times(1)).delete(post);

    }

    @Test
    public void testUpdatePostThrowsResourceNotFoundException()  {
        Post foundPost = Post.builder().id(postId)
                .userId(getDefaultUser().getId())
                .images(new ArrayList<>()).
                build();

        PostDTO expectedPostDTO = PostDTO.builder()
                .id(postId)
                .userId(getDefaultUser().getId())
                .images(new ArrayList<>())
                .build();

        given(postRepository.findById(foundPost.getId())).willAnswer(invocation -> {
            throw new NotFoundSocialNetworkException("post with id " + postId + " does not exists");
        });

        assertThrows(NotFoundSocialNetworkException.class, () -> {
      //      postService.updPost(postId, expectedPostDTO,anyList(),getDefaultUser());
        });
    }








}