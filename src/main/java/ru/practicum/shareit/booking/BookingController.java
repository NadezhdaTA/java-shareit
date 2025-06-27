package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.intrfaces.BookingServiceInterface;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingServiceInterface bookingService;
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingResponseDto postBooking(@RequestBody BookingRequestDto booking,
                                          @RequestHeader(USER_ID) Long bookerId) {
        return bookingService.postBooking(booking, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto bookingApprove(@PathVariable Long bookingId,
                                             @RequestHeader (USER_ID) Long ownerId,
                                             @RequestParam Boolean approved) {
        return bookingService.bookingApprove(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@PathVariable Long bookingId) {
        return bookingService.getBookingByBookingId(bookingId);
    }

    @GetMapping
    public Collection<BookingResponseDto> getBookingsForUser(
            @RequestHeader (USER_ID) Long userId) {
        return bookingService.getBookingsForUser(userId);
    }
}
