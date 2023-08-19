package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController controller;
    private MockMvc mvc;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .name("John")
                .email("John@example.com")
                .build();
    }

    @Test
    void addNewUser() throws Exception {
        when(userService.add(any(UserDto.class))).thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getById(1L)).thenReturn(userDto);

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void updateUser() throws Exception {
        UserDto updatedUserDto = UserDto.builder()
                .id(1L)
                .name("Updated Name")
                .email("John@example.com")
                .build();

        when(userService.update(any(UserDto.class), anyLong())).thenReturn(updatedUserDto);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(updatedUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updatedUserDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void getAll() throws Exception {
        when(userService.getAll()).thenReturn(List.of(userDto));
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$.[0].email", is(userDto.getEmail())));
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

    }
}
