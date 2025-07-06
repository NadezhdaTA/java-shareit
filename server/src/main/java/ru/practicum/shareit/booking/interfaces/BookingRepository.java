package ru.practicum.shareit.booking.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking save(Booking booking);

    Optional<Booking> getBookingByBookingId(Long bookingId);

    Collection<Booking> getBookingsByBooker_UserId(Long bookerId);

    Collection<Booking> getBookingsByItem_ItemId(Long itemId);

    @Query(value = "SELECT b FROM Booking b WHERE ?1 between b.start and b.end")
    Collection<Booking> getBookingsByBooker_UserIdCurrent(LocalDateTime date);

    @Query(value = "SELECT b FROM Booking b WHERE b.end < ?1")
    Collection<Booking> getBookingsByBooker_UserIdPast(LocalDateTime date);

    @Query(value = "SELECT b FROM Booking b WHERE b.start > ?1")
    Collection<Booking> getBookingsByBooker_UserIdFuture(LocalDateTime date);

    Collection<Booking> getBookingsByBooker_UserIdAndStatus(Long bookerId, BookingStatus status);

    @Query(value = "SELECT b FROM Booking b JOIN Item i on b.item.itemId = i.itemId WHERE i.owner.userId = ?1")
    Collection<Booking> getBookingsByOwnerId(Long ownerId);

    Collection<Booking> getBookingsByStatus(BookingStatus status);
}
