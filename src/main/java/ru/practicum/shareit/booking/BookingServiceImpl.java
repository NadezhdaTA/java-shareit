package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.intrfaces.BookingRepository;
import ru.practicum.shareit.booking.intrfaces.BookingServiceInterface;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AnotherUserException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.interfaces.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.interfaces.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingServiceInterface {
    private final BookingRepository storage;
    private final UserRepository userStorage;
    private final ItemRepository itemStorage;

    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;


    @Override
    public BookingResponseDto postBooking(BookingRequestDto booking, Long bookerId) {
        Item item = itemStorage.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (item.getIsAvailable().equals(false)) {
            throw new RuntimeException("Booking item is not available");
        }

        Booking saved = bookingMapper.bookingRequestDtoToBooking(booking);
        saved.setBooker(userStorage.getUserByUserId(bookerId).get());
        saved.setItem(item);

        if (item.getIsAvailable()) {
            saved.setStatus(BookingStatus.WAITING);
        } else {
            saved.setStatus(BookingStatus.REJECTED);
            throw new AnotherUserException("Booking item is rejected, item is not available");
        }

        storage.save(saved);

        BookingResponseDto responseDto = bookingMapper.bookingToBookingResponseDto(saved);
        mapBookerAndItemToBooking(responseDto, saved);
        return responseDto;
    }

    @Override
    public BookingResponseDto bookingApprove(Long bookingId, Long ownerId, Boolean isAvailable) {
        Booking booking = bookingRepository.getBookingByBookingId(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        Item item = booking.getItem();
        if (!item.getOwner().getUserId().equals(ownerId)) {
            throw new AnotherUserException("You are not allowed to approve this booking");
        }

        if (item.getIsAvailable().equals(false)) {
            booking.setStatus(BookingStatus.REJECTED);
            throw new RuntimeException("Booking item is rejected");
        } else {
            booking.setStatus(BookingStatus.APPROVED);
        }

        BookingResponseDto responseDto = bookingMapper.bookingToBookingResponseDto(booking);
        mapBookerAndItemToBooking(responseDto, booking);

        return responseDto;
    }

    @Override
    public BookingResponseDto getBookingByBookingId(Long bookingId) {
        Booking booking = bookingRepository.getBookingByBookingId(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        BookingResponseDto bookingDto = bookingMapper.bookingToBookingResponseDto(booking);

        mapBookerAndItemToBooking(bookingDto, booking);

        return bookingDto;
    }

    @Override
    public Collection<BookingResponseDto> getBookingsForUser(Long userId) {
        User user = userStorage.getUserByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Collection<Booking> bookings = bookingRepository.getBookingsByBooker_UserId(user.getUserId());
        Collection<BookingResponseDto> bookingDtos = new ArrayList<>();

        for (Booking booking : bookings) {
            BookingResponseDto bookingDto = bookingMapper.bookingToBookingResponseDto(booking);
            mapBookerAndItemToBooking(bookingDto, booking);
            bookingDtos.add(bookingDto);
        }

        return bookingDtos;
    }

    private void mapBookerAndItemToBooking(BookingResponseDto bookingDto, Booking booking) {
        bookingDto.setBooker(userMapper.toUserBookingDto(booking.getBooker()));
        bookingDto.setItem(itemMapper.toItemBookerDto(booking.getItem()));
    }
}
