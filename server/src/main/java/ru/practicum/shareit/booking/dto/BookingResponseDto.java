package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemBookerDto;
import ru.practicum.shareit.user.dto.UserBookingDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {

    @JsonProperty("id")
    private Long bookingId;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemBookerDto item;

    private UserBookingDto booker;

    private BookingStatus status;

}
