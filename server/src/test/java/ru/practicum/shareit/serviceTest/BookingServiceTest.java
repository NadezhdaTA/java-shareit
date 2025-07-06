package ru.practicum.shareit.serviceTest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AnotherUserException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;


import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Sql(value = "/test-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceTest extends TestData {
    final BookingServiceImpl bookingService;

     @Test
    public void addBookingTest() {
         BookingRequestDto bookingRequestDto = new BookingRequestDto();
         bookingRequestDto.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
         bookingRequestDto.setEnd(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
         bookingRequestDto.setItemId(itemBookerDto2.getItemId());

        BookingResponseDto bookingSaved = bookingService.addBooking(bookingRequestDto, user1.getUserId());

        assertAll(() -> {
            assertNotNull(bookingSaved);
            assertEquals(7, bookingSaved.getBookingId());
            assertEquals(bookingRequestDto.getStart(), bookingSaved.getStart());
            assertEquals(bookingRequestDto.getEnd(), bookingSaved.getEnd());
            assertEquals(BookingStatus.WAITING, bookingSaved.getStatus());
            assertEquals(userBookingDto1, bookingSaved.getBooker());
            assertEquals(itemBookerDto2, bookingSaved.getItem());
        });
    }

    @Test
    public void bookingApproveTest() {
        BookingResponseDto bookingResponseDto = bookingService.bookingApprove(booking2.getBookingId(), user2.getUserId(), false);
        assertAll(() -> {
            assertNotNull(bookingResponseDto);
            assertEquals(responseBooking2.getBookingId(), bookingResponseDto.getBookingId());
            assertEquals(responseBooking2.getStart(), bookingResponseDto.getStart());
            assertEquals(responseBooking2.getEnd(), bookingResponseDto.getEnd());
            assertEquals(responseBooking2.getStatus(), bookingResponseDto.getStatus());
            assertEquals(responseBooking2.getBooker().getUserId(), bookingResponseDto.getBooker().getUserId());
            assertEquals(responseBooking2.getItem().getItemId(), bookingResponseDto.getItem().getItemId());
        });
    }

    @Test
    public void bookingApproveWithWrongOwnerTest() {

        assertThrows(AnotherUserException.class, () -> {
            BookingResponseDto bookingResponseDto =
                    bookingService.bookingApprove(booking2.getBookingId(), user1.getUserId(), false);
        });
    }

    @Test
    public void bookingApproveWithUnavailableItemTest() {

        assertThrows(ValidationException.class, () ->
                bookingService.bookingApprove(booking1.getBookingId(), user1.getUserId(), true));
    }

    @Test
    public void getBookingByBookingIdTest() {
        BookingResponseDto found = bookingService.getBookingByBookingId(booking1.getBookingId(), user1.getUserId());
        assertAll(() -> {
            assertNotNull(found);
            assertEquals(found.getBookingId(), booking1.getBookingId());
            assertEquals(found.getStart(), booking1.getStart());
            assertEquals(found.getEnd(), booking1.getEnd());
            assertEquals(found.getStatus(), booking1.getStatus());
            assertEquals(found.getBooker().getUserId(), booking1.getBooker().getUserId());
            assertEquals(found.getItem().getItemId(), booking1.getItem().getItemId());
        });
    }

    @Test
    public void getBookingByUserIdWithWrongUserTest() {
        assertThrows(AnotherUserException.class, () ->
                bookingService.getBookingByBookingId(booking1.getBookingId(), 5L));
    }

    @Test
    public void getBookingsByUserWithWrongUserTest() {
        assertThrows(NotFoundException.class, () ->
                bookingService.getBookingsByUser(5L, "ALL"));
    }

    @Test
    public void getBookingsByUserWithWrongItemTest() {
        assertThrows(NotFoundException.class, () ->
                bookingService.getBookingsByUser(user3.getUserId(), "ALL"));
    }

    @Test
    public void getBookingsByUserALLTest() {
        Collection<BookingResponseDto> bookings = bookingService.getBookingsByUser(user1.getUserId(), "ALL");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(5, bookings.size());
        });
    }

    @Test
    public void getBookingsByUserCURRENTTest() {
        Collection<BookingResponseDto> bookings = bookingService.getBookingsByUser(user1.getUserId(), "CURRENT");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(2, bookings.size());
        });
    }

    @Test
    public void getBookingsByUserPastTest() {

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByUser(user1.getUserId(), "PAST");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(2, bookings.size());
        });
    }

    @Test
    public void getBookingsByUserFutureTest() {

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByUser(user1.getUserId(), "FUTURE");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(1, bookings.size());
            assertEquals(4L, bookings.iterator().next().getBookingId());
        });
    }

    @Test
    public void getBookingsByUserWaitingTest() {

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByUser(user1.getUserId(), "WAITING");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(3, bookings.size());
        });
    }

    @Test
    public void getBookingsByUserRejectedTest() {

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByUser(user1.getUserId(), "REJECTED");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(1, bookings.size());
        });
    }

    @Test
    public void addBookingWithUnavailableItemTest() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        bookingRequestDto.setEnd(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        bookingRequestDto.setItemId(itemBookerDto1.getItemId());

        assertThrows(ValidationException.class, () -> bookingService.addBooking(bookingRequestDto, user1.getUserId()));

    }

    @Test
    public void getBookingByOwnerTest() {
        Collection<BookingResponseDto> bookings = bookingService.getBookingsByOwner(itemResponseDto2.getOwnerId(), "ALL");
        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(2, bookings.size());
        });
    }

    @Test
    public void getBookingByOwnerCurrentTest() {
        Collection<BookingResponseDto> bookings = bookingService.getBookingsByOwner(itemResponseDto1.getOwnerId(), "CURRENT");
        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(2, bookings.size());
        });
    }

    @Test
    public void getBookingByOwnerPastTest() {
        Collection<BookingResponseDto> bookings = bookingService.getBookingsByOwner(itemResponseDto1.getOwnerId(), "PAST");
        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(1, bookings.size());
        });
    }

    @Test
    public void getBookingByOwnerFutureTest() {
        Collection<BookingResponseDto> bookings = bookingService.getBookingsByOwner(itemResponseDto1.getOwnerId(), "FUTURE");
        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(1, bookings.size());
        });
    }

    @Test
    public void getBookingByOwnerWaitingTest() {
        Collection<BookingResponseDto> bookings = bookingService.getBookingsByOwner(itemResponseDto1.getOwnerId(), "WAITING");
        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(3, bookings.size());
        });
    }

    @Test
    public void getBookingByOwnerRejectedTest() {
        Collection<BookingResponseDto> bookings = bookingService.getBookingsByOwner(itemResponseDto1.getOwnerId(), "REJECTED");
        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(1, bookings.size());
        });
    }
}
