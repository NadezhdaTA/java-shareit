package ru.practicum.shareit.mapperTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserAuthorDto;
import ru.practicum.shareit.user.dto.UserBookingDto;
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
    public void userToUserRequestDtoTest() {
        User user = new User(1L, "TestName", "test@test.com");

        UserRequestDto userRequestDto = userMapper.toUserRequestDto(user);

        assertAll(() -> {
               assertEquals(1L, userRequestDto.getUserId());
               assertEquals("TestName", userRequestDto.getUserName());
               assertEquals("test@test.com", userRequestDto.getUserEmail());
        });
    }

    @Test
    public void userRequestDtoToUserTest() {
        UserRequestDto userRequestDto = new UserRequestDto(2L, "TestName2", "test2@test.com");
        User user = userMapper.toUser(userRequestDto);
        assertAll(() -> {
            assertEquals(2L, user.getUserId());
            assertEquals("TestName2", user.getUserName());
            assertEquals("test2@test.com", user.getUserEmail());
        });
    }

    @Test
    public void userResponseDtoToUserTest() {
        UserResponseDto userResponseDto = new UserResponseDto(3L, "TestName3", "test3@test.com");
        User user = userMapper.toUser(userResponseDto);
        assertAll(() -> {
            assertEquals(3L, user.getUserId());
            assertEquals("TestName3", user.getUserName());
            assertEquals("test3@test.com", user.getUserEmail());
        });
    }

    @Test
    public void userToUserResponseDtoTest() {
        User user = new User(4L, "TestName4", "test4@test.com");
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
        assertAll(() -> {
            assertEquals(4L, userResponseDto.getUserId());
            assertEquals("TestName4", userResponseDto.getUserName());
            assertEquals("test4@test.com", userResponseDto.getUserEmail());
        });
    }

    @Test
    public void userToUserAuthorDtoTest() {
        User user = new User(5L, "TestName5", "test5@test.com");

        UserAuthorDto userAuthorDto = userMapper.toUserAuthorDto(user);

        assertAll(() -> {
            assertEquals(5L, userAuthorDto.getUserId());
            assertEquals("TestName5", userAuthorDto.getUserName());
        });
    }

    @Test
    public void userToUserBookingDtoTest() {
        User user = new User(6L, "TestName6", "test6@test.com");

        UserBookingDto userBookingDto = userMapper.toUserBookingDto(user);

        assertAll(() -> {
            assertEquals(6L, userBookingDto.getUserId());
            assertEquals("TestName6", userBookingDto.getUserName());
        });
    }
}
