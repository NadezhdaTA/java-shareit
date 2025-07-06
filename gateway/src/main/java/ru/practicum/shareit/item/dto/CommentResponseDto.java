package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserAuthorDto;

@Data
public class CommentResponseDto {

    @JsonProperty("id")
    private Long commentId;

    @JsonProperty("text")
    private String text;

    private UserAuthorDto author;
}
