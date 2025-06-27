package ru.practicum.shareit.mapperTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemBookerDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.UserBookingDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookingMapperTest {
    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ItemMapper itemMapper;

    User user =new User(1L, "testName1", "test1@test.com");

    ItemRequest itemRequest = new ItemRequest(1L, "testDescription1",
            1L, LocalDate.of(2025, 6, 25));

    Item item = new Item(1L, "TestName1", "Test description1", true, user, itemRequest);
    LocalDateTime startTime = LocalDateTime.of(2025, 6, 25, 12, 30);
    LocalDateTime endTime = LocalDateTime.of(2025, 6, 27, 13, 30);

    @Test
    public void bookingRequestDtoToBookingTest() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();

        bookingRequestDto.setBookingId(1L);
        bookingRequestDto.setStart(startTime);
        bookingRequestDto.setEnd(endTime);
        bookingRequestDto.setStatus(BookingStatus.WAITING);

        Booking booking = bookingMapper.bookingRequestDtoToBooking(bookingRequestDto);
        assertAll(() -> {
            assertEquals(booking.getBookingId(), bookingRequestDto.getBookingId());
            assertEquals(booking.getStart(), bookingRequestDto.getStart());
            assertEquals(booking.getEnd(), bookingRequestDto.getEnd());
            assertEquals(booking.getStatus(), bookingRequestDto.getStatus());
        });
    }

    @Test
    public void bookingToBookingResponseDtoTest() {
        Booking booking = new Booking(3L, startTime, endTime, item, user, BookingStatus.APPROVED);

        BookingResponseDto bookingResponseDto = bookingMapper.bookingToBookingResponseDto(booking);

        ItemBookerDto itemBookerDto = itemMapper.toItemBookerDto(item);
        UserBookingDto userBookingDto = userMapper.toUserBookingDto(user);
        assertAll(() -> {
            assertEquals(booking.getBookingId(), bookingResponseDto.getBookingId());
            assertEquals(booking.getStart(), bookingResponseDto.getStart());
            assertEquals(booking.getEnd(), bookingResponseDto.getEnd());
            assertEquals(booking.getStatus(), bookingResponseDto.getStatus());
            assertEquals(userBookingDto, bookingResponseDto.getBooker());
            assertEquals(itemBookerDto, bookingResponseDto.getItem());
        });
    }
}
