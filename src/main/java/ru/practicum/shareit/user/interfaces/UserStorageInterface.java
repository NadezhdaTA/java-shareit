package ru.practicum.shareit.user.interfaces;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorageInterface {
    User createUser(User user);

    Optional<User> getUserById(Long userId);

    User updateUser(Long userId, User user);

    Collection<User> getAllUsers();

    void deleteUser(Long userId);

    void deleteAllUsers();
}
