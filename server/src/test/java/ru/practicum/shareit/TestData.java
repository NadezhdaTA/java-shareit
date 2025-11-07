package ru.practicum.shareit;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.request.dto.RequestInputDto;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.request.dto.RequestOutputWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserAuthorDto;
import ru.practicum.shareit.user.dto.UserBookingDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class TestData {
    public static User user1 = new User(1L, "TestUser1", "user1@mail.ru");
    public static UserBookingDto userBookingDto1 = new UserBookingDto(1L, "TestUser1");
    public static UserAuthorDto userAuthorDto1 = new UserAuthorDto(1L, "TestUser1");
    public static UserRequestDto userRequestDto1 =
            new UserRequestDto(1L, "TestUser1", "user1@mail.ru");

    public static UserRequestDto userForCreate() {
        UserRequestDto user = new UserRequestDto();
        user.setUserName("TestUser1");
        user.setUserEmail("user1@mail.ru");
        return user;
    }

    public static UserRequestDto user2ForCreate() {
        UserRequestDto user = new UserRequestDto();
        user.setUserName("TestUser2");
        user.setUserEmail("user2@mail.ru");
        return user;
    }

    public static UserResponseDto userResponseDto1 =
            new UserResponseDto(1L, "TestUser1", "user1@mail.ru");


    public static User user2 = new User(2L, "TestUser2", "user2@mail.ru");
    public static UserBookingDto userBookingDto2 =
            new UserBookingDto(2L, "TestUser2");
    public static UserRequestDto userRequestDto2 =
            new UserRequestDto(2L, "TestUser2", "user2@mail.ru");
    public static UserResponseDto userResponseDto2 =
            new UserResponseDto(2L, "TestUser2", "user2@mail.ru");


    public static ItemRequest request1 = new ItemRequest(1L, "Test request description1", user1,
            LocalDateTime.of(2025, 7, 7, 10, 15, 25));
    public static RequestInputDto requestInputDto1 = new RequestInputDto(1L, "Test request description1",
            LocalDateTime.of(2025, 7, 7, 10, 15, 25));
    public static RequestOutputDto requestOutputDto1 = new RequestOutputDto(
            1L, "Test request description1", userBookingDto1,
            LocalDateTime.of(2025, 7, 7, 10, 15, 25));
    public static RequestOutputWithItemsDto requestWithItemsDto1 = new RequestOutputWithItemsDto(1L,
            "Test request description1", userBookingDto1,
            LocalDateTime.of(2025, 7, 7, 10, 15, 25), new ArrayList<>());


    public static ItemRequestDto itemForCreate() {
        ItemRequestDto request = new ItemRequestDto();
        request.setItemName("testItem1");
        request.setItemDescription("test description1");
        request.setIsAvailable(true);
        return request;
    }

    public static ItemRequestDto item2ForCreate() {
        ItemRequestDto request = new ItemRequestDto();
        request.setItemName("testItem2");
        request.setItemDescription("test description2");
        request.setIsAvailable(false);
        return request;
    }

    public static ItemRequestDto itemForUpdate() {
        ItemRequestDto request = new ItemRequestDto();
        request.setItemName("testItem1");
        request.setItemDescription("test description1");
        request.setIsAvailable(false);
        return request;
    }

    public static ItemRequest request2 = new ItemRequest(2L, "Test request description2", user2,
            LocalDateTime.of(2025, 8, 8, 15, 10, 20));
    public static RequestOutputDto requestOutputDto2 = new RequestOutputDto(
            2L, "Test request description2",
            userBookingDto2, LocalDateTime.of(2025, 8, 8, 15, 10, 20));


    public static List<RequestOutputDto> requests = List.of(requestOutputDto1);
    public static List<RequestOutputDto> allRequests = List.of(requestOutputDto1, requestOutputDto2);


    public static ItemRequestDto itemRequestDto1 = new ItemRequestDto(1L, "testItem1",
            "test description1", false, request1.getItemRequestId());
    public static ItemBookerDto itemBookerDto1 = new ItemBookerDto(1L, "testItem1");
    public static ItemResponseDto itemResponseDto1 = new ItemResponseDto(1L, "testItem1",
            "test description1", false, user1.getUserId(), request1.getItemRequestId());
    public static ItemResponseDtoWithComments itemWithComments1 = new ItemResponseDtoWithComments(
            1L, "testItem1", "test description1", false,
            user1.getUserId(), request1.getItemRequestId(), new BookingResponseDto(), new BookingResponseDto(), new ArrayList<>());


    public static ItemBookerDto itemBookerDto2 = new ItemBookerDto(2L, "testItem2");
    public static ItemResponseDto itemResponseDto2 = new ItemResponseDto(2L, "testItem2",
            "test description2", true, user2.getUserId(), request2.getItemRequestId());

    public Collection<ItemResponseDto> itemsForOwner = List.of(itemResponseDto1);
    public Collection<ItemResponseDto> itemsFound = List.of(itemResponseDto2);


    public static BookingResponseDto responseBooking1 = new BookingResponseDto(1L,
            LocalDateTime.of(2025, 7, 7, 14, 40, 35),
            LocalDateTime.of(2025, 7, 15, 18, 12, 43),
            itemBookerDto1, userBookingDto1, BookingStatus.WAITING);

    public static BookingRequestDto bookingRequestDto1 = new BookingRequestDto(1L,
            LocalDateTime.of(2025, 7, 7, 14, 40, 35),
            LocalDateTime.of(2025, 7, 15, 18, 12, 43),
            itemBookerDto1.getItemId(), userBookingDto1.getUserId(), BookingStatus.WAITING);


    public static BookingRequestDto booking1ForCreate() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(LocalDateTime.of(2025, 7, 7, 14, 40, 35));
        bookingRequestDto.setEnd(LocalDateTime.of(2025, 7, 25, 18, 12, 43));
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setBookerId(1L);
        bookingRequestDto.setStatus(BookingStatus.WAITING);
        return bookingRequestDto;
    }

    public static BookingResponseDto responseBooking2 = new BookingResponseDto(
            2L, LocalDateTime.of(2025, 8, 8, 15, 45, 30),
            LocalDateTime.of(2025, 8, 18, 12, 10, 40),
            itemBookerDto2, userBookingDto2, BookingStatus.APPROVED);

    public static BookingResponseDto responseBooking3 = new BookingResponseDto(
            3L, LocalDateTime.of(2025, 3, 7, 14, 40, 35),
            LocalDateTime.of(2025, 5, 25, 18, 12, 43),
            itemBookerDto1, userBookingDto1, BookingStatus.WAITING);

    public static BookingResponseDto responseBooking4 = new BookingResponseDto(
            4L, LocalDateTime.of(2025, 9, 7, 14, 40, 35),
            LocalDateTime.of(2025, 10, 25, 18, 12, 43),
            itemBookerDto1, userBookingDto1, BookingStatus.WAITING);

    public static BookingResponseDto responseBooking5 = new BookingResponseDto(
            5L, LocalDateTime.of(2025, 5, 7, 14, 40, 35),
            LocalDateTime.of(2025, 10, 25, 18, 12, 43),
            itemBookerDto1, userBookingDto1, BookingStatus.REJECTED);

    public Collection<BookingResponseDto> bookings =
            Arrays.asList(responseBooking1, responseBooking2, responseBooking3, responseBooking4, responseBooking5);

    public Collection<BookingResponseDto> bookingsForOwner =
            Arrays.asList(responseBooking1, responseBooking3, responseBooking4, responseBooking5);

    public static CommentResponseCreatedDto createdDto =
            new CommentResponseCreatedDto(1L, "Test comment text1", userAuthorDto1.getUserName(), true);

}



