package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserBookingDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestOutputWithItemsDto {
    @JsonProperty("id")
    private Long itemRequestId;

    @JsonProperty("description")
    private String requestDescription;

    private UserBookingDto requester;

    @JsonProperty("created")
    private LocalDateTime requestDate;

    @JsonProperty("items")
    Collection<ItemResponseDto> items;
}
