package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.interfaces.UserServiceInterface;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    public UserResponseDto createUser(UserRequestDto user) {
        validateEmail(user.getUserEmail());
        User newUser = userMapper.toUser(user);
        return userMapper.toUserResponseDto(userStorage.createUser(newUser));
    }

    public UserResponseDto getUserById(Long userId) throws NotFoundException {
        return userMapper.toUserResponseDto(userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found, id = " + userId)));
    }

    public UserResponseDto updateUser(Long userId, UserRequestDto user) throws NotFoundException {
        validateEmail(user.getUserEmail());
        return userMapper.toUserResponseDto(userStorage.updateUser(userId, userMapper.toUser(user)));
    }

    public void deleteUser(Long userId) throws NotFoundException {
        getUserById(userId);
        userStorage.deleteUser(userId);
    }

    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }

    private void validateEmail(String email) {
        Collection<User> users = userStorage.getAllUsers();

        if (!users.isEmpty()) {
            Optional<String> first =
                    userStorage.getAllUsers().stream()
                            .map(User::getUserEmail)
                            .filter(userEmail -> userEmail.equals(email))
                            .findFirst();

            if (first.isPresent()) {
                throw new DuplicatedDataException("User with email " + email + " already exists");
            }
        }
    }
}
