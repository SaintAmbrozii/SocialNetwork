package com.example.socialnetwork.controller;


import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.payload.UserDTO;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.service.UserService;

import com.example.socialnetwork.servicetest.UserServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
;


import java.security.Principal;
import java.util.*;


import static com.example.socialnetwork.Const.*;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String URL_PREFIX = "/api/users";
    private static final Long USER_ID = 1L;
    private static final String USERNAME = "user1";


    @MockBean
    private UserService userService;




    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserRepo repository;

     @Autowired
    MockMvc mockMvc;

    Principal principal;

    @BeforeEach
    public void init() {
        principal = new Principal() {

            @Override
            public String getName() {
                return USERNAME;
            }
        };

    }

    public static User getDefaultUser() {
        var result = User.builder().id(ID).name(NAME).lastname(SECOND_NAME).
                password(PASSWORD).email(EMAIL).phone(PHONE).build();


        return result;
    }

    @Test
    void createUser() throws Exception {
        User user = getDefaultUser();

        Mockito.when(userService.createUser(user)).thenReturn(user);



        mockMvc.perform(post("/api/users").content(mapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))

               .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name", is("name")))
                .andExpect((ResultMatcher) jsonPath("$.email",is("email@email.com")))
                .andReturn();

    }
    @Test
    void testGetAllUsers() throws Exception {
        List<UserDTO> users = new ArrayList<>();
        users.add(new UserDTO());
        users.add(new UserDTO());
        when(userService.getAllUsers()).thenReturn(users);
        mockMvc.perform(get(URL_PREFIX)).andExpect(status().isOk())
                .andExpect((ResultMatcher)jsonPath("$.size",is(users.size())));
    }

    @Test
    void getUserId() throws Exception {
        Long id = 1L;
        User user = getDefaultUser();

        when(userService.findById(id)).thenReturn(user);
        mockMvc.perform(get("/api/users/{id}",id)).andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value(id))
                .andExpect((ResultMatcher) jsonPath("$.name").value("name"));
    }

    @Test
    void updateUser() throws Exception {
        User user = User.builder()
                .id(USER_ID)
                .name(USERNAME)
                .email("user@test.com").subscribers(Set.of(User.builder().id(2L).build(), User.builder().id(3L).build()))
                .build();
        when(userService.updateUser(USER_ID,user)).thenReturn(user);
        mockMvc.perform(put("/api/users/{id}",USER_ID).content(mapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id").value(user.getId()))
                .andExpect((ResultMatcher) jsonPath("$.name").value("user1"));
    }

    void deleteUser() throws Exception {
        User user = getDefaultUser();
        mockMvc.perform(delete("/api/users/{id}",user.getId())).andExpect(status().isNoContent());
    }



















}
