package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @JsonProperty("description")
    private String requestDescription;

    private LocalDateTime requestDate = LocalDateTime.now();
}
