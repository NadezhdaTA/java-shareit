package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDtoWithComments {

    @JsonProperty("id")
    private Long itemId;

    @JsonProperty("name")
    private String itemName;

    @JsonProperty("description")
    private String itemDescription;

    @JsonProperty("available")
    private Boolean isAvailable;

    @JsonProperty("userId")
    private Long ownerId;

    private Long requestId;

    private BookingResponseDto lastBooking;

    private BookingResponseDto nextBooking;

    private List<CommentResponseDto> comments;
}
