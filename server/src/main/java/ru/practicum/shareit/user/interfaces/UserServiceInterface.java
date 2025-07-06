package ru.practicum.shareit.user.interfaces;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;


public interface UserServiceInterface {
    UserResponseDto createUser(UserRequestDto user);

    UserResponseDto getUserById(Long userId);

    UserResponseDto updateUser(Long userId, UserRequestDto user);

    void deleteUser(Long userId);

    void deleteAllUsers();

}
