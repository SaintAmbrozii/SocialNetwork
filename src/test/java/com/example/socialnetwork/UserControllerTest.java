package com.example.socialnetwork;

import aj.org.objectweb.asm.TypeReference;
import com.example.socialnetwork.controller.UserController;
import com.example.socialnetwork.domain.Role;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.payload.UserDTO;
import com.example.socialnetwork.repo.UserRepo;
import com.example.socialnetwork.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.socialnetwork.Const.*;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
@WithMockUser("spring")
public class UserControllerTest {

    private static final String URL_PREFIX = "/api/users";
    private static final Long USER_ID = 1L;
    private static final String USERNAME = "user1";

    public static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "User not found for this id : ";

    @InjectMocks
    UserService userService;


    ObjectMapper objectMapper;
    @Autowired
    UserRepo repository;

    private MockMvc mockMvc;
    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    public static User getDefaultUser() {
        var result = User.builder().id(ID).name(NAME).lastname(SECOND_NAME).
                password(PASSWORD).email(EMAIL).phone(PHONE).build();
        result.setRoles(Collections.singleton(Role.USER));

        return result;
    }

    @Test
    @DisplayName("Get by ID")
    void getByIdFound() throws Exception {
        var input = getDefaultUser();
        when(userService.findById(any())).thenReturn(getDefaultUser());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL_PREFIX+ "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var output = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(input.getId(), output.getId());
        assertEquals(input.getEmail(), output.getEmail());
    }

















}
