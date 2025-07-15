package ru.practicum.shareit.serviceTest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.interfaces.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = "jdbc.url=jdbc:h2:mem:testdb;MODE=PostgreSQL",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceTest extends TestData {
    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    @Test
    void addUserTest() {
        UserRequestDto forCreation = new UserRequestDto();
        forCreation.setUserName("User3");
        forCreation.setUserEmail("User3@mail.ru");
        UserResponseDto userResponseDto = userService.createUser(forCreation);

        assertAll(() ->
                assertNotNull(userResponseDto));
                assertThat(forCreation).usingRecursiveComparison().ignoringFields("userId")
                        .isEqualTo(userResponseDto);
    }

    @Test
    void createUser_WithWrongEmailTest() {
        userService.createUser(userForCreate());
        assertThrows(DuplicatedDataException.class,
                () -> userService.createUser(userRequestDto1));
    }

    @Test
    void getUserByIdTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        UserResponseDto found = userService.getUserById(user.getUserId());

        assertAll(() -> {
            assertNotNull(found);
            assertThat(found).usingRecursiveComparison().isEqualTo(user);
        });
    }

    @Test
    void getUserById_NotFoundExceptionTest() {
        assertThrows(NotFoundException.class,
                () -> userService.getUserById(5L));
    }

    @Test
    void updateUserTest() {

        UserResponseDto user = userService.createUser(userForCreate());

        UserRequestDto forUpdate = new UserRequestDto();
        forUpdate.setUserName("UpdatedName");
        forUpdate.setUserEmail("UpdatedEmail@mail.ru");

        UserResponseDto updated = new UserResponseDto(user.getUserId(), "UpdatedName", "UpdatedEmail@mail.ru");

        UserResponseDto userResponseDto = userService.updateUser(user.getUserId(), forUpdate);
        assertAll(() -> {
            assertEquals(user.getUserId(), userResponseDto.getUserId());
            assertThat(updated).usingRecursiveComparison().ignoringFields("userId").isEqualTo(userResponseDto);
        });
    }

    @Test
    void updateUserTest_NotFoundExceptionTest() {
        UserRequestDto forUpdate = new UserRequestDto();
        forUpdate.setUserName("UpdatedName");
        forUpdate.setUserEmail("UpdatedEmail@mail.ru");

        assertThrows(NotFoundException.class, () -> userService.updateUser(5L, forUpdate));
    }

    @Test
    void deleteUserByIdTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        userService.deleteUser(user.getUserId());

        assertThrows(NotFoundException.class, () -> userService.getUserById(user.getUserId()));

    }

    @Test
    void deleteUserById_NotFoundExceptionTest() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(5L));
    }

    @Test
    void deleteAllUserTest() {
        Collection<User> users = userRepository.findAll();
        assertNotNull(users);

        userService.deleteAllUsers();

        assertEquals(0, userRepository.findAll().size());
    }

}
