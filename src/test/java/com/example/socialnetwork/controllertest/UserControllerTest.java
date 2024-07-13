package com.example.socialnetwork.controllertest;

import com.example.socialnetwork.controller.UserController;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exception.NotFoundSocialNetworkException;
import com.example.socialnetwork.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.socialnetwork.Const.SECOND_ID;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final String URL_PREFIX = "/api/users";
    private static final Long USER_ID = 1L;
    private static final String NAME = "Kadirov";

    public static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "User not found for this id : ";

    @MockBean
    private UserService userService;



    @Autowired
    MockMvc mockMvc;



    @Autowired
    private ObjectMapper objectMapper;



 //   @Test
 //   public void testGetAllUsers() throws Exception {
 //       List<UserDTO> users = new ArrayList<>();
  //      users.add(new UserDTO());
  ////      users.add(new UserDTO());
  //      when(userService.getAll()).thenReturn(users);
  //      mockMvc.perform(get(URL_PREFIX)).andExpect(status().isOk())
  //              .andExpect(jsonPath("$.size()", is(users.size())));
  //  }

    @Test
    public void testFindUserById() throws Exception {

        User user = User.builder()
                .id(USER_ID)
                .name(NAME)
 //               .email("user@test.com")
                .build();

        when(userService.findById(USER_ID)).thenReturn(user);
        mockMvc.perform(get(URL_PREFIX + "/" + USER_ID)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.name", is(NAME)))
   //             .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andReturn();
    }

    @Test
    void testFindByIdShouldThrowException() throws Exception {

        User user = User.builder()
                .id(USER_ID)
                .name(NAME)
   //             .email("user@test.com")
                .build();

        when(userService.findById(USER_ID)).thenAnswer(invocation -> {
            return new NotFoundSocialNetworkException(USER_NOT_FOUND_EXCEPTION_MESSAGE + USER_ID);
        });
        mockMvc.perform(get(URL_PREFIX  + USER_ID)).andExpect(status().isNotFound());
    }

    @Test
    public void testSubscribeByUser() throws Exception {


        mockMvc.perform(put(URL_PREFIX + "/" + SECOND_ID + "/" +"subscribe")).andExpect(status().isOk());
   //     verify(userService, times(1)).subscribeByUser(any(User.class), anyLong());

    }

    @Test
    public void unSubcribe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put( URL_PREFIX + "/"+SECOND_ID + "/" +"unsubscribe"))
                .andExpect(status().isOk());
        verify(userService, times(1)).unSubscribeByUser(any(), any());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = User.builder()
                .id(USER_ID)
                .name(NAME)
       //         .email("kadirov@test.com")
                .build();
    //    when(userService.updateUser(USER_ID, user)).thenReturn(user);
        mockMvc.perform(put(URL_PREFIX + "/" + USER_ID).content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email", is("kadirov@test.com")))
                .andReturn();
    }
    @Test

    public void getSubscribers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PREFIX + "/" + USER_ID + "/"+"subscribers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).getAllSubscribers(USER_ID);
    }


    @Test
    public void getSubscriprions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PREFIX + "/" + USER_ID + "/"+"subscriptions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).getAllSubscription(USER_ID);
    }

    @Test
    public void deleteSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PREFIX+ "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getByIdNotFound() throws Exception {
        when(userService.findById(USER_ID)).thenThrow(NotFoundSocialNetworkException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PREFIX + "/" +USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }










}
