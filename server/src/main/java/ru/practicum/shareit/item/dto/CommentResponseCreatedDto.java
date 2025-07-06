package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseCreatedDto {
    @JsonProperty("id")
    private Long commentId;

    @JsonProperty("text")
    private String text;

    private String authorName;

    private Boolean created = true;
}
