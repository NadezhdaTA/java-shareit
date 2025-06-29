package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
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
    public BookingResponseDto addBooking(@RequestBody @Valid BookingRequestDto booking,
                                         @RequestHeader(USER_ID) Long bookerId) {
        return bookingService.addBooking(booking, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto bookingApprove(@PathVariable Long bookingId,
                                             @RequestHeader (USER_ID) Long ownerId,
                                             @RequestParam Boolean approved) {
        return bookingService.bookingApprove(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@PathVariable Long bookingId,
                                         @RequestHeader(USER_ID) Long userId) {
        return bookingService.getBookingByBookingId(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingResponseDto> getBookingsByUser(@RequestHeader (USER_ID) Long userId,
                                                            @RequestParam(name = "state", defaultValue = "all") String state) {
        return bookingService.getBookingsByUser(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingResponseDto> getBookingsByOwner(@RequestHeader (USER_ID) Long userId,
                                                             @RequestParam(name = "state", defaultValue = "all") String state) {
        return bookingService.getBookingsByOwner(userId, state);
    }
}
