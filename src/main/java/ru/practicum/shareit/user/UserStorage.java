package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.interfaces.UserStorageInterface;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserStorage implements UserStorageInterface {

    private final Map<Long, User> users = new HashMap<>();
    private Long nextUserId = 1L;

    public User createUser(User user) {
        user.setUserId(nextUserId++);
        users.put(user.getUserId(), user);
        return user;
    }

    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public User updateUser(Long userId, User user) {
        User oldUser = users.get(userId);

        if (user.getUserName() != null) {
            oldUser.setUserName(user.getUserName());
        }

        if (user.getUserEmail() != null) {
            oldUser.setUserEmail(user.getUserEmail());
        }

        return oldUser;
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    public void deleteAllUsers() {
        users.clear();
    }
}
