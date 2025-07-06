package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

@RestController
@Validated
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PatchMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable long id, @RequestBody UserRequestDto user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteAllUsers(@PathVariable long id) {
        userService.deleteUser(id);
    }

    @DeleteMapping
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

}
