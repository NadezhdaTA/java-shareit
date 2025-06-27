package ru.practicum.shareit.booking.intrfaces;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.Collection;

public interface BookingServiceInterface {
    BookingResponseDto postBooking(BookingRequestDto booking, Long bookerId);

    BookingResponseDto bookingApprove(Long bookingId, Long ownerId, Boolean isAvailable);

    BookingResponseDto getBookingByBookingId(Long bookingId);

    Collection<BookingResponseDto> getBookingsForUser(Long userId);
}
