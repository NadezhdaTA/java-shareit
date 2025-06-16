package ru.practicum.shareit.booking;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
public class Booking {

    private Long bookingId;
    private LocalDate start;
    private LocalDate end;
    private Long itemId;
    private Long bookerId;
    BookingStatus status;
}
