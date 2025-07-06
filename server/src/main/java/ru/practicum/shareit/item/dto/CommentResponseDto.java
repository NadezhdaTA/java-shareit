package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserAuthorDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {

    @JsonProperty("id")
    private Long commentId;

    @JsonProperty("text")
    private String text;

    private UserAuthorDto author;
}
