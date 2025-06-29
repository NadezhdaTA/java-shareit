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
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.interfaces.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.interfaces.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

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
    public BookingResponseDto addBooking(BookingRequestDto booking, Long bookerId) {
        Item item = itemStorage.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        User booker = userStorage.getUserByUserId(bookerId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Booking saved = bookingMapper.bookingRequestDtoToBooking(booking);
        saved.setBooker(booker);
        saved.setItem(item);

        if (item.getIsAvailable().equals(false)) {
            saved.setStatus(BookingStatus.REJECTED);
            storage.save(saved);
            throw new RuntimeException("Booking is rejected, item is not available");
        } else {
            saved.setStatus(BookingStatus.WAITING);
            storage.save(saved);
        }

        return mapBookerAndItemToBooking(saved);
    }

    @Override
    public BookingResponseDto bookingApprove(Long bookingId, Long ownerId, Boolean isAvailable) {
        Booking booking = bookingRepository.getBookingByBookingId(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        Item item = itemStorage.getItemByItemId(booking.getItem().getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (!item.getOwner().getUserId().equals(ownerId)) {
            throw new AnotherUserException("You are not allowed to approve this booking");
        }

        if (item.getIsAvailable().equals(false)) {
            booking.setStatus(BookingStatus.REJECTED);
            throw new ValidationException("Booking item is rejected");
        } else {
            booking.setStatus(BookingStatus.APPROVED);
            bookingRepository.save(booking);
        }

        return mapBookerAndItemToBooking(booking);
    }

    @Override
    public BookingResponseDto getBookingByBookingId(Long bookingId, Long userId) {
        Booking booking = bookingRepository.getBookingByBookingId(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if(booking.getBooker().getUserId().equals(userId) ||
                booking.getItem().getOwner().getUserId().equals(userId)) {
            return mapBookerAndItemToBooking(booking);
        } else {
            throw new AnotherUserException("You are not allowed to view this booking");
        }
    }

    @Override
    public Collection<BookingResponseDto> getBookingsByUser(Long userId, String state) {
        User user = userStorage.getUserByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        LocalDateTime date = LocalDateTime.now();

        Collection<Booking> bookings = new ArrayList<>();

        if (state.equalsIgnoreCase("ALL")) {
            bookings = bookingRepository.getBookingsByBooker_UserId(userId);

        } else if (state.equalsIgnoreCase("CURRENT") ||
                    state.equalsIgnoreCase("PAST") ||
                    state.equalsIgnoreCase("FUTURE")) {

            bookings = getBookingsByTimeState(date, state).stream()
                    .filter(booking -> booking.getBooker().getUserId().equals(userId))
                    .toList();

        } else if (state.equalsIgnoreCase("WAITING") ||
                    state.equalsIgnoreCase("REJECTED")) {

            if (state.equalsIgnoreCase("WAITING")) {
                bookings = bookingRepository.getBookingsByBooker_UserIdAndStatus(userId, BookingStatus.WAITING);
            } else {
                bookings = bookingRepository.getBookingsByBooker_UserIdAndStatus(userId, BookingStatus.REJECTED);
            }
        }

        return bookings.stream()
                .map(this::mapBookerAndItemToBooking)
                .sorted(Comparator.comparing(BookingResponseDto::getStart))
                .toList()
                .reversed();
    }

    @Override
    public Collection<BookingResponseDto> getBookingsByOwner(Long userId, String state) {
        User user = userStorage.getUserByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item = itemStorage.getItemsByOwner_UserId(userId).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User with userId = " + userId + " has no Item"));

        LocalDateTime date = LocalDateTime.now();

        Collection<Booking> bookings = new ArrayList<>();

        if (state.equalsIgnoreCase("ALL")) {
            bookings = bookingRepository.getBookingsByOwnerId(userId);
        } else if (state.equalsIgnoreCase("CURRENT") ||
                state.equalsIgnoreCase("PAST") ||
                state.equalsIgnoreCase("FUTURE")) {

            bookings = getBookingsByTimeState(date, state).stream()
                    .filter(booking -> booking.getItem().getOwner().getUserId().equals(userId))
                    .toList();

        } else if (state.equalsIgnoreCase("WAITING") ||
                state.equalsIgnoreCase("REJECTED")) {

            if (state.equalsIgnoreCase("WAITING")) {
                bookings = bookingRepository.getBookingsByStatus(BookingStatus.WAITING).stream()
                        .filter(booking -> booking.getItem().getOwner().getUserId().equals(userId))
                        .toList();
            } else {
                bookings = bookingRepository.getBookingsByStatus(BookingStatus.REJECTED).stream()
                        .filter(booking -> booking.getItem().getOwner().getUserId().equals(userId))
                        .toList();
            }
        }
        return bookings.stream()
                .map(this::mapBookerAndItemToBooking)
                .sorted(Comparator.comparing(BookingResponseDto::getStart))
                .toList()
                .reversed();
    }


    private BookingResponseDto mapBookerAndItemToBooking(Booking booking) {
        BookingResponseDto bookingDto = bookingMapper.bookingToBookingResponseDto(booking);
        bookingDto.setBooker(userMapper.toUserBookingDto(booking.getBooker()));
        bookingDto.setItem(itemMapper.toItemBookerDto(booking.getItem()));
        return bookingDto;
    }

    private Collection<Booking> getBookingsByTimeState(LocalDateTime date, String state) {
        Collection<Booking> bookings = new ArrayList<>();
        switch (state.toUpperCase()) {
            case "CURRENT" -> bookings = bookingRepository.getBookingsByBooker_UserIdCurrent(date);
            case "PAST" -> bookings = bookingRepository.getBookingsByBooker_UserIdPast(date);
            case "FUTURE" -> bookings = bookingRepository.getBookingsByBooker_UserIdFuture(date);
        }
        return bookings;
    }
}

