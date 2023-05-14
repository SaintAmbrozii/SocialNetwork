package com.example.socialnetwork;

import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.payload.PostDTO;
import com.example.socialnetwork.repo.PostRepo;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.service.FileService;
import com.example.socialnetwork.service.PostService;
import com.example.socialnetwork.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

import static com.example.socialnetwork.Const.*;
import static com.example.socialnetwork.UserServiceTest.getDefaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIllegalStateException;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PostServiceTest {

    @Mock
     PostRepo postRepository;
    @Mock
    FileService fileService;


    @InjectMocks
    UserService userService;

    @InjectMocks
    PostService postService;


    private static final Long postId = 1L;
    public static User getDefaultUser() {
        var result = User.builder().id(ID).name(NAME).lastname(SECOND_NAME).
                password(PASSWORD).email(EMAIL).phone(PHONE).build();
        result.setRoles(Collections.singleton(Role.USER));

        return result;
    }


    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);



    }


    public static Post getDefaultPost()  {
        var result = new Post();
        result.setId(ID);
        result.setText("text");
        for (int i = 0; i < LIKES; i++)
            result.plusDislikes();
        for (int i = 0; i < DISLIKES; i++)
            result.minusDislikes();

        var comment = new Comment();
        comment.setAuthor(getDefaultUser());
        comment.setPost(result);

        result.getComments().add(comment);
        return result;
    }
    public static   Post getDefaultPost2() {

        var result = new Post();
        result.setId(ID);
        result.setText("text");
        for (int i = 0; i < LIKES; i++)
            result.plusDislikes();
        for (int i = 0; i < DISLIKES; i++)
            result.minusDislikes();

        var comment = new Comment();
        comment.setAuthor(getDefaultUser());
        comment.setPost(result);

        result.getComments().add(comment);
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
    public void testDeletePost()  {
        var user = getDefaultUser();
        Post post = Post.builder()
                .id(postId)
                .text("text")
                .author(user)
                .images(new ArrayList<>())
                .comments(new ArrayList<>()).build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

   //     postService.deletePost(post,user);

    }

    @Test
    public void addPost() throws IOException {


        User user = getDefaultUser();
       MultipartFile file = IMAGE_MOCK_MULTIPART_FILE;
   //    Image image = fileService.toImageEntity(file);
        String text =new String("text");

        Post post = new Post();
        post.setId(postId);
 //       post.addImage(image);
        post.setText(text);
        post.setAuthor(user);

        when(postRepository.save(post)).thenReturn(post);
//        postService.postWithImg(text,file,user);
        assertEquals(postId,post.getId());
        assertEquals(text,post.getText());
        assertEquals(user,post.getAuthor());

    }

    @Test
    public void testUpdatePost() throws IOException {
       var user = getDefaultUser();
        MultipartFile file = IMAGE_MOCK_MULTIPART_FILE;
 //       Image image = fileService.toImageEntity(file);
        Long id = postId;
        String text =new String(TEXT);
        Post post = new Post();
    //   post.addImage(image);
        post.setText(text);
        post.setAuthor(user);

       Post updPost = new Post();
   //     updPost.addImage(image);
        updPost.setText(text);
       updPost.setAuthor(user);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));


   //     postService.updPost(updPost.getId(),updPost.getText(),file,null);
        assertThat(post).usingRecursiveComparison().isEqualTo(updPost);
        when(postRepository.save(post)).thenReturn(post);
        verifyNoMoreInteractions(postRepository);

    }



    @Test
    public void testGetAllPosts() {
        BDDMockito.given(postRepository.findAll()).willReturn(List.of(new Post(),new Post(),new Post()));
        assertThat(postRepository.findAll()).hasSize(3);
        verify(postRepository, times(1)).findAll();
    }


    @Test
    public void getById() {
        Post post = Post.builder()
                .id(postId)
                .text("text").build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        postService.getById(post.getId());
        assertNotNull(post);
        assertEquals(postId,post.getId());

        verifyNoMoreInteractions(postRepository);
    }


    private Post reactionTest(Reactions reactions,  int LikesCount,
                               int DislikesCount, boolean beforeAddLikedPostToUser) throws IOException {
        var processedPost = getDefaultPost();
        var processedUser = getDefaultUser();


        if (beforeAddLikedPostToUser) {
            processedUser.addLikedPost(processedPost);
            processedUser.addDislikedPost(processedPost);
        }

        when(postRepository.findById(any())).thenReturn(Optional.of(processedPost));

        when(postRepository.save(any())).thenReturn(processedPost);


        postService.react(processedPost.getId(),processedUser,reactions);


        verify(postRepository, times(1)).save(processedPost);
        verify(userService, times(1)).update(processedUser);

        assertEquals(LikesCount, processedPost.getLikes().intValue());
        assertEquals(DislikesCount, processedPost.getDisLikes().intValue());

        return processedPost;
    }
    @Test
    public void likeSuccessful() throws IOException {
       reactionTest(Reactions.LIKE,LIKES+1,DISLIKES,false);
    }






}
