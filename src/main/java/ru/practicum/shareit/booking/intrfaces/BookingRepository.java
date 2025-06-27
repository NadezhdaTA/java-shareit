package ru.practicum.shareit.booking.intrfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking save(Booking booking);

    Optional<Booking> getBookingByBookingId(Long bookingId);

    Collection<Booking> getBookingsByBooker_UserId(Long bookerId);

    Collection<Booking> getBookingsByItem_ItemId(Long itemId);
}
