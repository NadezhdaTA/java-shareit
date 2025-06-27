package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserAuthorDto;
import ru.practicum.shareit.user.dto.UserBookingDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel="spring")
public interface UserMapper {

    UserRequestDto toUserRequestDto(User user);

    User toUser(UserRequestDto userDto);

    UserResponseDto toUserResponseDto(User user);

    User toUser(UserResponseDto userDto);

    UserAuthorDto toUserAuthorDto(User user);

    UserBookingDto toUserBookingDto(User booker);

}
