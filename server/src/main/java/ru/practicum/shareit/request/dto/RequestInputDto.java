package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestInputDto {
    @JsonProperty("id")
    private Long itemRequestId;

    @JsonProperty("description")
    private String requestDescription;

    private LocalDateTime requestDate = LocalDateTime.now();
}
