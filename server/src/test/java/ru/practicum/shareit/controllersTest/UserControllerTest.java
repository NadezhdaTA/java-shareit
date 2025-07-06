package ru.practicum.shareit.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserServiceImpl;

import org.springframework.http.MediaType;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class UserControllerTest extends TestData {
    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void createUserTest() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(userResponseDto2);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userRequestDto2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userResponseDto2.getUserId()), Long.class))
                .andExpect(jsonPath("$.name", is(userResponseDto2.getUserName()), String.class))
                .andExpect(jsonPath("$.email", is(userResponseDto2.getUserEmail()), String.class));
    }

    @Test
    void getUserTest() throws Exception {
        when(userService.getUserById(any()))
                .thenReturn(userResponseDto1);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userResponseDto1.getUserId()), Long.class))
                .andExpect(jsonPath("$.name", is(userResponseDto1.getUserName()), String.class))
                .andExpect(jsonPath("$.email", is(userResponseDto1.getUserEmail()), String.class));

    }

    @Test
    void updateUserTest() throws Exception {
        UserResponseDto forUpdate = new UserResponseDto();
        forUpdate.setUserId(1L);
        forUpdate.setUserName("UpdatedName");
        forUpdate.setUserEmail("UpdatedEmail@mail.ru");

        when(userService.updateUser(anyLong(), any()))
                .thenReturn(forUpdate);

        MvcResult result = mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(forUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        UserResponseDto responseDto = mapper.readValue(responseBody, UserResponseDto.class);
        assertAll(() -> {
            assertNotNull(responseDto);
            assertEquals("UpdatedName", responseDto.getUserName());
            assertEquals("UpdatedEmail@mail.ru", responseDto.getUserEmail());
        });
    }

    @Test
    void deleteUserTest() throws Exception {
       mvc.perform(delete("/users/1")
                       .characterEncoding(StandardCharsets.UTF_8)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    void getAllUsersTest() throws Exception {
        mvc.perform(delete("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}



