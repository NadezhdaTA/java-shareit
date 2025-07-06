package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.interfaces.UserRepository;
import ru.practicum.shareit.user.interfaces.UserServiceInterface;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserServiceInterface {

    private final UserRepository userStorage;
    private final UserMapper userMapper;

    public UserResponseDto createUser(UserRequestDto user) {
        validateEmail(user.getUserEmail());
        User newUser = userMapper.toUser(user);
        return userMapper.toUserResponseDto(userStorage.save(newUser));
    }

    public UserResponseDto getUserById(Long userId) throws NotFoundException {
        return userMapper.toUserResponseDto(userStorage.getUserByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User not found, id = " + userId)));
    }

    public UserResponseDto updateUser(Long userId, UserRequestDto user) throws NotFoundException {
        User updated = userStorage.getUserByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User not found, id = " + userId));

        validateEmail(user.getUserEmail());

        if (user.getUserEmail() != null) {
            updated.setUserEmail(user.getUserEmail());
        }

        if (user.getUserName() != null) {
            updated.setUserName(user.getUserName());
        }

        return userMapper.toUserResponseDto(userStorage.save(updated));
    }

    public void deleteUser(Long userId) throws NotFoundException {
        User user = userMapper.toUser(getUserById(userId));
        userStorage.delete(user);
    }

    public void deleteAllUsers() {
        userStorage.deleteAll();
    }

    private void validateEmail(String email) {
        Collection<User> users = userStorage.findAll();

        if (!users.isEmpty()) {
            Optional<String> first =
                    userStorage.findAll().stream()
                            .map(User::getUserEmail)
                            .filter(userEmail -> userEmail.equals(email))
                            .findFirst();

            if (first.isPresent()) {
                throw new DuplicatedDataException("User with email " + email + " already exists");
            }
        }
    }
}
