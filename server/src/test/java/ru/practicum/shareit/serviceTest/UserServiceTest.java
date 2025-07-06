package ru.practicum.shareit.serviceTest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
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
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Sql(value = "/test-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        assertThrows(DuplicatedDataException.class,
                () -> userService.createUser(userRequestDto1));
    }

    @Test
    void getUserByIdTest() {
        UserResponseDto found = userService.getUserById(1L);

        assertAll(() -> {
            assertNotNull(found);
            assertThat(found).usingRecursiveComparison().isEqualTo(userResponseDto1);
        });
    }

    @Test
    void getUserById_NotFoundExceptionTest() {
        assertThrows(NotFoundException.class,
                () -> userService.getUserById(5L));
    }

    @Test
    void updateUserTest() {
        UserRequestDto forUpdate = new UserRequestDto();
        forUpdate.setUserName("UpdatedName");
        forUpdate.setUserEmail("UpdatedEmail@mail.ru");

        UserResponseDto updated = new UserResponseDto(1L, "UpdatedName", "UpdatedEmail@mail.ru");

        UserResponseDto userResponseDto = userService.updateUser(1L, forUpdate);
        assertAll(() -> {
            assertEquals(userBookingDto1.getUserId(), userResponseDto.getUserId());
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
        assertEquals(userResponseDto1, userService.getUserById(1L));

        userService.deleteUser(1L);

        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));

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
