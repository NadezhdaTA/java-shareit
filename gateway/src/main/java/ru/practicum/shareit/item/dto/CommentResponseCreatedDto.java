package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentResponseCreatedDto {
    @JsonProperty("id")
    private Long commentId;

    @JsonProperty("text")
    private String text;

    private String authorName;

    private Boolean created = true;
}
