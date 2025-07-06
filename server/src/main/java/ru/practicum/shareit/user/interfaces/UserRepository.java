package ru.practicum.shareit.user.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);

    Optional<User> getUserByUserId(Long userId);

    List<User> findAll();

    void deleteUserByUserId(Long id);

    void deleteAll();
}
