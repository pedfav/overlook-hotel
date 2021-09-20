package com.pedfav.overlookhotel.gateway.http.controllers;

import com.pedfav.overlookhotel.entities.User;
import com.pedfav.overlookhotel.exceptions.EmailAlreadyTakenException;
import com.pedfav.overlookhotel.exceptions.ResourceNotFoundException;
import com.pedfav.overlookhotel.gateway.http.RestResponseEntityExceptionHandler;
import com.pedfav.overlookhotel.gateway.http.converters.UserConverter;
import com.pedfav.overlookhotel.usecases.UserUseCase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static com.pedfav.overlookhotel.fixtures.UserFixture.user;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Mock
    private UserUseCase userUseCase;

    @InjectMocks
    private UserController userController;

    private final UserConverter userConverter = new UserConverter();

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(userController, "userConverter", userConverter);

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    void testGetUserByIdSuccessfully() throws Exception {
        User user = user();
        when(userUseCase.getUserById(any())).thenReturn(user);

        mockMvc.perform(get("/users/1")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(user.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        when(userUseCase.getUserById(any())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/users/1")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testListUser() throws Exception {
        User user = user();
        when(userUseCase.listUsers(any(), any())).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/users")
                .param("page", "0")
                .param("page_size", "20")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(Matchers.hasSize(1)));
    }

    @Test
    void testListUserEmpty() throws Exception {
        when(userUseCase.listUsers(any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users")
                .param("page", "0")
                .param("page_size", "20")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(Matchers.hasSize(0)));
    }

    @Test
    void testCreateUser() throws Exception {
        User user = user();
        when(userUseCase.createUser(any(User.class))).thenReturn(user);
        String content = "{ \"name\": \"Jack Torrance\", \"email\": \"jaockta@gmail.com\", \"birthday\": \"10/10/1962\" }";

        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(content))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUserMissingFields() throws Exception {
        User user = user();
        when(userUseCase.createUser(any(User.class))).thenReturn(user);
        String content = "{ \"name\": \"Jack Torrance\", \"birthday\": \"10/10/1962\" }";

        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUserEmailAlreadyTaken() throws Exception {
        when(userUseCase.createUser(any(User.class))).thenThrow(EmailAlreadyTakenException.class);
        String content = "{ \"name\": \"Jack Torrance\", \"email\": \"jaockta@gmail.com\", \"birthday\": \"10/10/1962\" }";

        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(content))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testModifyUser() throws Exception {
        User user = user();
        when(userUseCase.modifyUser(anyLong(), any(User.class))).thenReturn(user);
        String content = "{ \"name\": \"Jack Torrance\", \"email\": \"jaockta@gmail.com\", \"birthday\": \"10/10/1962\" }";

        mockMvc.perform(put("/users/1")
                .contentType("application/json")
                .content(content))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUserByIdSuccessfully() throws Exception {

        mockMvc.perform(delete("/users/1")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}



