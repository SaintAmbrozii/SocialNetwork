package com.example.socialnetwork.servicetest;

import com.example.socialnetwork.domain.Role;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.*;

import static com.example.socialnetwork.Const.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    PasswordEncoder encoder;

    @Mock
     UserRepo userRepo;

    @InjectMocks
     UserService userService;

    private static final Long userId  = 1L;
    private static final String encodedPassword = "dnasdsandsadoasdndsandsadiosadnsada";


    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);



    }

    public static User getDefaultUser() {
        var result = User.builder().id(ID).name(NAME).lastname(SECOND_NAME).
               password(PASSWORD).email(EMAIL).phone(PHONE).build();
     //   result.setRoles(Collections.singleton(Role.USER));

       return result;
    }
    public static User getSecondUser() {
        var result = User.builder().id(SECOND_ID).name(SECOND_NAME).lastname(SECOND_NAME).
                password(SECOND_PASSWORD).email(SECOND_EMAIL).phone(PHONE).build();
  //      result.setRoles(Collections.singleton(Role.USER));

        return result;
    }

    @Test
    public void testCreateUser() {
        User NEWuser = getDefaultUser();

        when(userRepo.findUserByEmail(getDefaultUser().getEmail())).thenReturn(null);
        when(encoder.encode(getDefaultUser().getPassword())).thenReturn(encodedPassword);


        when(userRepo.save(getDefaultUser())).thenReturn(getDefaultUser());
        verifyNoMoreInteractions(userRepo);

    }

    @Test
    public void deleteUser()  {
        User user = getDefaultUser();

    //    userService.deleteUser(user);

        verify(userRepo, times(1)).delete(user);
    }

    @Test
    public void testGetAllUsers() {
        BDDMockito.given(userRepo.findAll()).willReturn(List.of(new User(), new User(), new User()));

    //    assertThat(userService.getAll()).hasSize(3);
        verify(userRepo, times(1)).findAll();
    }

    @Test
    public void testUpdateUser()  {
        User expectedUser = getDefaultUser();

        when(userRepo.findById(userId)).thenReturn(Optional.of(expectedUser));
        when(userRepo.save(expectedUser)).thenReturn(expectedUser);

    //    User actualUser = userService.updateUser(userId, expectedUser);

    //    assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);
        verify(userRepo, times(1)).findById(userId);
        verify(userRepo, times(1)).save(expectedUser);
        verifyNoMoreInteractions(userRepo);
    }





















}
