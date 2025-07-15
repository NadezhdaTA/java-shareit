package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingService;
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestBody @Valid BookingRequestDto booking,
                                         @RequestHeader(USER_ID) Long bookerId) {
        return bookingService.addBooking(bookerId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> bookingApprove(@PathVariable Long bookingId,
                                                 @RequestHeader (USER_ID) Long ownerId,
                                                 @RequestParam Boolean approved) {
        return bookingService.bookingApprove(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable Long bookingId,
                                             @RequestHeader(USER_ID) Long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUser(@RequestHeader (USER_ID) Long userId,
                                                    @RequestParam(name = "state", defaultValue = "all") String state) {
        return bookingService.getBookingsByUser(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader (USER_ID) Long userId,
                                                     @RequestParam(name = "state", defaultValue = "all") BookingState state) {
        return bookingService.getBookingsByOwner(userId, state);
    }
}
