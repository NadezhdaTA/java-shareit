package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserBookingDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestOutputDto {
    @JsonProperty("id")
    private Long itemRequestId;

    @JsonProperty("description")
    private String requestDescription;

    private UserBookingDto requester;

    @JsonProperty("created")
    private LocalDateTime createdAt;
}
