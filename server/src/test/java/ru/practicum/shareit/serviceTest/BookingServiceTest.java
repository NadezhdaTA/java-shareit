package ru.practicum.shareit.serviceTest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AnotherUserException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserResponseDto;


import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = "jdbc.url=jdbc:h2:mem:testdb;MODE=PostgreSQL",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingServiceTest extends TestData {
    private final BookingServiceImpl bookingService;
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;

    @Test
    public void addBookingTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        bookingRequestDto.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        bookingRequestDto.setItemId(item.getItemId());
        BookingResponseDto bookingSaved = bookingService.addBooking(bookingRequestDto, user.getUserId());

        assertAll(() -> {
            assertNotNull(bookingSaved);
            assertEquals(bookingRequestDto.getStart(), bookingSaved.getStart());
            assertEquals(bookingRequestDto.getEnd(), bookingSaved.getEnd());
            assertEquals(BookingStatus.WAITING, bookingSaved.getStatus());
            assertEquals(user.getUserId(), bookingSaved.getBooker().getUserId());
            assertEquals(item.getItemId(), bookingSaved.getItem().getItemId());
        });
    }

    @Test
    public void bookingApproveTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        bookingRequestDto.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        bookingRequestDto.setItemId(item.getItemId());
        BookingResponseDto bookingSaved = bookingService.addBooking(bookingRequestDto, user.getUserId());

        BookingResponseDto bookingResponseDto = bookingService.bookingApprove(bookingSaved.getBookingId(), user.getUserId(), false);

        assertAll(() -> {
            assertNotNull(bookingResponseDto);
            assertEquals(BookingStatus.APPROVED, bookingResponseDto.getStatus());
        });
    }

    @Test
    public void bookingApproveWithWrongOwnerTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        bookingRequestDto.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        bookingRequestDto.setItemId(item.getItemId());
        BookingResponseDto bookingSaved = bookingService.addBooking(bookingRequestDto, user.getUserId());

        assertThrows(AnotherUserException.class, () -> {
            BookingResponseDto bookingResponseDto =
                    bookingService.bookingApprove(bookingSaved.getBookingId(), 25L, false);
        });
    }

    @Test
    public void bookingApproveWithUnavailableItemTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        bookingRequestDto.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        bookingRequestDto.setItemId(item.getItemId());
        BookingResponseDto bookingSaved = bookingService.addBooking(bookingRequestDto, user.getUserId());

        ItemRequestDto itemForUpdate = new ItemRequestDto();
        itemForUpdate.setIsAvailable(false);
        itemService.updateItem(item.getItemId(), itemForUpdate, user.getUserId());

        assertThrows(ValidationException.class, () ->
                bookingService.bookingApprove(bookingSaved.getBookingId(), user.getUserId(), false));
    }

    @Test
    public void getBookingByBookingIdTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        bookingRequestDto.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        bookingRequestDto.setItemId(item.getItemId());
        BookingResponseDto bookingSaved = bookingService.addBooking(bookingRequestDto, user.getUserId());

        assertAll(() -> {
            assertNotNull(bookingSaved);
            assertEquals(LocalDateTime.of(2025, 8, 8, 15, 45, 30), bookingSaved.getStart());
            assertEquals(LocalDateTime.of(2025, 8, 18, 12, 10, 40), bookingSaved.getEnd());
            assertEquals(BookingStatus.WAITING, bookingSaved.getStatus());
            assertEquals(user.getUserId(), bookingSaved.getBooker().getUserId());
            assertEquals(item.getItemId(), bookingSaved.getItem().getItemId());
        });
    }

    @Test
    public void getBookingByUserIdWithWrongUserTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());
        UserResponseDto user1 = userService.createUser(user2ForCreate());

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        bookingRequestDto.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        bookingRequestDto.setItemId(item.getItemId());
        BookingResponseDto bookingResponseDto = bookingService.addBooking(bookingRequestDto, user.getUserId());

        assertThrows(AnotherUserException.class, () ->
                bookingService.getBookingByBookingId(bookingResponseDto.getBookingId(), user1.getUserId()));
    }

    @Test
    public void getBookingsByUserWithWrongUserTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        bookingRequestDto.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        bookingRequestDto.setItemId(item.getItemId());
        bookingService.addBooking(bookingRequestDto, user.getUserId());

        assertThrows(NotFoundException.class, () ->
                bookingService.getBookingsByUser(125L, "ALL"));
    }

    @Test
    public void getBookingsByUserALLTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        BookingRequestDto booking1 = booking1ForCreate();
        booking1.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        booking1.setEnd(LocalDateTime.of(2025, 7, 25, 18, 12, 43));
        booking1.setItemId(item.getItemId());
        booking1.setBookerId(user.getUserId());
        bookingService.addBooking(booking1, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        booking2.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        booking2.setItemId(item.getItemId());
        booking2.setBookerId(user.getUserId());
        bookingService.addBooking(booking2, user.getUserId());

        BookingRequestDto booking3 = booking1ForCreate();
        booking3.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        booking3.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        booking3.setItemId(item.getItemId());
        booking3.setBookerId(user.getUserId());
        bookingService.addBooking(booking3, user.getUserId());

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByUser(user.getUserId(), "ALL");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(3, bookings.size());
        });
    }

    @Test
    public void getBookingsByUserCURRENTTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        BookingRequestDto booking1 = booking1ForCreate();
        booking1.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        booking1.setEnd(LocalDateTime.of(2025, 7, 25, 18, 12, 43));
        booking1.setItemId(item.getItemId());
        booking1.setBookerId(user.getUserId());
        bookingService.addBooking(booking1, user.getUserId());

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByUser(user.getUserId(), "CURRENT");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(1, bookings.size());
        });
    }

    @Test
    public void getBookingsByUserPastTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        BookingRequestDto booking1 = booking1ForCreate();
        booking1.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        booking1.setEnd(LocalDateTime.of(2025, 7, 25, 18, 12, 43));
        booking1.setItemId(item.getItemId());
        booking1.setBookerId(user.getUserId());
        bookingService.addBooking(booking1, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 3, 7, 14, 40, 35));
        booking2.setEnd(LocalDateTime.of(2025, 5, 25, 18, 12, 43));
        booking2.setItemId(item.getItemId());
        booking2.setBookerId(user.getUserId());
        bookingService.addBooking(booking2, user.getUserId());

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByUser(user.getUserId(), "PAST");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(1, bookings.size());
        });
    }

    @Test
    public void getBookingsByUserFutureTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        BookingRequestDto booking1 = booking1ForCreate();
        booking1.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        booking1.setEnd(LocalDateTime.of(2025, 7, 25, 18, 12, 43));
        booking1.setItemId(item.getItemId());
        booking1.setBookerId(user.getUserId());
        bookingService.addBooking(booking1, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        booking2.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        booking2.setItemId(item.getItemId());
        booking2.setBookerId(user.getUserId());
        bookingService.addBooking(booking2, user.getUserId());


        Collection<BookingResponseDto> bookings = bookingService.getBookingsByUser(user.getUserId(), "FUTURE");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(1, bookings.size());
        });
    }

    @Test
    public void getBookingsByUserWaitingTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        BookingRequestDto booking1 = booking1ForCreate();
        booking1.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        booking1.setEnd(LocalDateTime.of(2025, 7, 25, 18, 12, 43));
        booking1.setItemId(item.getItemId());
        booking1.setBookerId(user.getUserId());
        bookingService.addBooking(booking1, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        booking2.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        booking2.setItemId(item.getItemId());
        booking2.setBookerId(user.getUserId());
        bookingService.addBooking(booking2, user.getUserId());

        BookingRequestDto booking3 = booking1ForCreate();
        booking3.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        booking3.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        booking3.setItemId(item.getItemId());
        booking3.setBookerId(user.getUserId());
        bookingService.addBooking(booking3, user.getUserId());

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByUser(user.getUserId(), "WAITING");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(3, bookings.size());
        });
    }

    @Test
    public void getBookingsByUserRejectedTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());
        ItemResponseDto item2 = itemService.createItem(item2ForCreate(), user.getUserId());

        BookingRequestDto booking1 = booking1ForCreate();
        booking1.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        booking1.setEnd(LocalDateTime.of(2025, 7, 25, 18, 12, 43));
        booking1.setItemId(item.getItemId());
        booking1.setBookerId(user.getUserId());
        bookingService.addBooking(booking1, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        booking2.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        booking2.setItemId(item.getItemId());
        booking2.setBookerId(user.getUserId());
        bookingService.addBooking(booking2, user.getUserId());

        BookingRequestDto booking3 = booking1ForCreate();
        booking3.setStart(LocalDateTime.of(2025, 5, 7, 14, 40, 35));
        booking3.setEnd(LocalDateTime.of(2025, 10, 25, 18, 12, 43));
        booking3.setItemId(item2.getItemId());
        booking3.setBookerId(user.getUserId());
        assertThrows(ValidationException.class,
                () -> bookingService.addBooking(booking3, user.getUserId()));

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByUser(user.getUserId(), "REJECTED");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(1, bookings.size());
        });
    }

    @Test
    public void addBookingWithUnavailableItemTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        bookingRequestDto.setEnd(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        bookingRequestDto.setItemId(item.getItemId());
        ItemResponseDto itemUpdated = itemService.updateItem(item.getItemId(), itemForUpdate(), user.getUserId());

        assertThrows(ValidationException.class, () -> bookingService.addBooking(bookingRequestDto, user.getUserId()));

    }

    @Test
    public void getBookingByOwnerTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());
        UserResponseDto user1 = userService.createUser(user2ForCreate());

        BookingRequestDto booking1 = booking1ForCreate();
        booking1.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        booking1.setEnd(LocalDateTime.of(2025, 7, 25, 18, 12, 43));
        booking1.setItemId(item.getItemId());
        booking1.setBookerId(user1.getUserId());
        bookingService.addBooking(booking1, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        booking2.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        booking2.setItemId(item.getItemId());
        booking2.setBookerId(user.getUserId());
        bookingService.addBooking(booking2, user.getUserId());

        BookingRequestDto booking3 = booking1ForCreate();
        booking3.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        booking3.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        booking3.setItemId(item.getItemId());
        booking3.setBookerId(user.getUserId());
        bookingService.addBooking(booking3, user.getUserId());

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByOwner(item.getOwnerId(), "ALL");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(3, bookings.size());
        });
    }

    @Test
    public void getBookingByOwnerCurrentTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());
        UserResponseDto user1 = userService.createUser(user2ForCreate());

        BookingRequestDto booking1 = booking1ForCreate();
        booking1.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        booking1.setEnd(LocalDateTime.of(2025, 7, 25, 18, 12, 43));
        booking1.setItemId(item.getItemId());
        booking1.setBookerId(user1.getUserId());
        bookingService.addBooking(booking1, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 3, 7, 14, 40, 35));
        booking2.setEnd(LocalDateTime.of(2025, 5, 25, 18, 12, 43));
        booking2.setItemId(item.getItemId());
        booking2.setBookerId(user1.getUserId());
        bookingService.addBooking(booking2, user.getUserId());

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByOwner(item.getOwnerId(), "CURRENT");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(1, bookings.size());
        });
    }

    @Test
    public void getBookingByOwnerPastTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());
        UserResponseDto user1 = userService.createUser(user2ForCreate());

        BookingRequestDto booking1 = booking1ForCreate();
        booking1.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        booking1.setEnd(LocalDateTime.of(2025, 7, 25, 18, 12, 43));
        booking1.setItemId(item.getItemId());
        booking1.setBookerId(user1.getUserId());
        bookingService.addBooking(booking1, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 3, 7, 14, 40, 35));
        booking2.setEnd(LocalDateTime.of(2025, 5, 25, 18, 12, 43));
        booking2.setItemId(item.getItemId());
        booking2.setBookerId(user1.getUserId());
        bookingService.addBooking(booking2, user.getUserId());

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByOwner(item.getOwnerId(), "PAST");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(1, bookings.size());
        });
    }

    @Test
    public void getBookingByOwnerFutureTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());
        UserResponseDto user1 = userService.createUser(user2ForCreate());

        BookingRequestDto booking1 = booking1ForCreate();
        booking1.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        booking1.setEnd(LocalDateTime.of(2025, 7, 25, 18, 12, 43));
        booking1.setItemId(item.getItemId());
        booking1.setBookerId(user1.getUserId());
        bookingService.addBooking(booking1, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        booking2.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        booking2.setItemId(item.getItemId());
        booking2.setBookerId(user.getUserId());
        bookingService.addBooking(booking2, user.getUserId());

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByOwner(item.getOwnerId(), "FUTURE");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(1, bookings.size());
        });
    }

    @Test
    public void getBookingByOwnerWaitingTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());
        UserResponseDto user1 = userService.createUser(user2ForCreate());

        BookingRequestDto booking1 = booking1ForCreate();
        booking1.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        booking1.setEnd(LocalDateTime.of(2025, 7, 25, 18, 12, 43));
        booking1.setItemId(item.getItemId());
        booking1.setBookerId(user1.getUserId());
        bookingService.addBooking(booking1, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        booking2.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        booking2.setItemId(item.getItemId());
        booking2.setBookerId(user.getUserId());
        bookingService.addBooking(booking2, user.getUserId());

        BookingRequestDto booking3 = booking1ForCreate();
        booking3.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        booking3.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        booking3.setItemId(item.getItemId());
        booking3.setBookerId(user.getUserId());
        bookingService.addBooking(booking3, user.getUserId());

        Collection<BookingResponseDto> bookings = bookingService.getBookingsByOwner(item.getOwnerId(), "WAITING");
        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(3, bookings.size());
        });
    }

    @Test
    public void getBookingByOwnerRejectedTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        ItemResponseDto item = itemService.createItem(itemForCreate(), user.getUserId());
        UserResponseDto user1 = userService.createUser(user2ForCreate());
        ItemResponseDto item2 = itemService.createItem(item2ForCreate(), user.getUserId());

        BookingRequestDto booking1 = booking1ForCreate();
        booking1.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        booking1.setEnd(LocalDateTime.of(2025, 7, 25, 18, 12, 43));
        booking1.setItemId(item.getItemId());
        booking1.setBookerId(user1.getUserId());
        bookingService.addBooking(booking1, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 8, 8, 15, 45, 30));
        booking2.setEnd(LocalDateTime.of(2025, 8, 18, 12, 10, 40));
        booking2.setItemId(item.getItemId());
        booking2.setBookerId(user1.getUserId());
        bookingService.addBooking(booking2, user.getUserId());

        BookingRequestDto booking3 = booking1ForCreate();
        booking3.setStart(LocalDateTime.of(2025, 5, 7, 14, 40, 35));
        booking3.setEnd(LocalDateTime.of(2025, 10, 25, 18, 12, 43));
        booking3.setItemId(item2.getItemId());
        booking3.setBookerId(user1.getUserId());
        assertThrows(ValidationException.class,
                () -> bookingService.addBooking(booking3, user.getUserId()));


        Collection<BookingResponseDto> bookings = bookingService.getBookingsByOwner(item.getOwnerId(), "REJECTED");

        assertAll(() -> {
            assertNotNull(bookings);
            assertEquals(1, bookings.size());
        });
    }

}
