package ru.practicum.shareit.user.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring", uses = UserMapper
        .class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserRequestDto toUserRequestDto(User user);

    User toUser(UserRequestDto userDto);

    UserResponseDto toUserResponseDto(User user);

    User toUser(UserResponseDto userDto);
}
