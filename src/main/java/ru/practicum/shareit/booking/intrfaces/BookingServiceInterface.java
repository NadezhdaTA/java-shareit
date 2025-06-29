package ru.practicum.shareit.booking.intrfaces;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.Collection;

public interface BookingServiceInterface {
    BookingResponseDto addBooking(BookingRequestDto booking, Long bookerId);

    BookingResponseDto bookingApprove(Long bookingId, Long ownerId, Boolean isAvailable);

    BookingResponseDto getBookingByBookingId(Long bookingId, Long userId);

    Collection<BookingResponseDto> getBookingsByUser(Long userId, String state);

    Collection<BookingResponseDto> getBookingsByOwner(Long userId, String state);
}
