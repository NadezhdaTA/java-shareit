package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentRequestDto {

    @JsonProperty("id")
    private Long authorId;

    @JsonProperty("text")
    private String text;

    private LocalDateTime date = LocalDateTime.now();

}
