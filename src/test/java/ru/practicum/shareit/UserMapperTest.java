package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void UserToUserRequestDtoTest() {
        User user = new User(1L, "TestName", "test@test.com");

        UserRequestDto userRequestDto = userMapper.toUserRequestDto(user);

        assertAll(() -> {
               assertEquals(1L, userRequestDto.getUserId());
               assertEquals("TestName", userRequestDto.getUserName());
               assertEquals("test@test.com", userRequestDto.getUserEmail());
        });
    }

    @Test
    public void UserRequestDtoToUserTest() {
        UserRequestDto userRequestDto = new UserRequestDto(2L, "TestName2", "test2@test.com");
        User user = userMapper.toUser(userRequestDto);
        assertAll(() -> {
            assertEquals(2L, user.getUserId());
            assertEquals("TestName2", user.getUserName());
            assertEquals("test2@test.com", user.getUserEmail());
        });
    }

    @Test
    public void UserResponseDtoToUserTest() {
        UserResponseDto userResponseDto = new UserResponseDto(3L, "TestName3", "test3@test.com");
        User user = userMapper.toUser(userResponseDto);
        assertAll(() -> {
            assertEquals(3L, user.getUserId());
            assertEquals("TestName3", user.getUserName());
            assertEquals("test3@test.com", user.getUserEmail());
        });
    }

    @Test
    public void UserToUserResponseDtoTest() {
        User user = new User(4L, "TestName4", "test4@test.com");
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
        assertAll(() -> {
            assertEquals(4L, userResponseDto.getUserId());
            assertEquals("TestName4", userResponseDto.getUserName());
            assertEquals("test4@test.com", userResponseDto.getUserEmail());
        });
    }
}
