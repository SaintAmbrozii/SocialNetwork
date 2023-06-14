package com.example.socialnetwork.controller;

import aj.org.objectweb.asm.TypeReference;
import com.example.socialnetwork.controller.UserController;
import com.example.socialnetwork.domain.Role;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.payload.UserDTO;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.socialnetwork.Const.*;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WithMockUser("spring")
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final String URL_PREFIX = "/api/users";
    private static final Long USER_ID = 1L;
    private static final String USERNAME = "user1";

    public static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "User not found for this id : ";

    @MockBean
    private UserService userService;



    private ObjectMapper mapper;

    @MockBean
    private UserRepo repository;


    private MockMvc mockMvc;

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
        result.setRoles(Collections.singleton(Role.USER));

        return result;
    }

    @Test
    void createUser() throws Exception {
        User user = getDefaultUser();
        Mockito.when(userService.createUser(user)).thenReturn(user);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", notNullValue() ))
                .andExpect((ResultMatcher) jsonPath("$.name", is("name")));
    }


















}
