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
import ru.practicum.shareit.exception.AnotherUserException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.request.RequestServiceImpl;
import ru.practicum.shareit.request.dto.RequestInputDto;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = "jdbc.url=jdbc:h2:mem:testdb;MODE=PostgreSQL",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemServiceTest extends TestData {
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final RequestServiceImpl requestService;
    private final BookingServiceImpl bookingService;

    @Test
    public void addItemTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);

        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        assertAll(() -> {
            assertNotNull(saved);
            assertThat(forCreation).usingRecursiveComparison()
                    .ignoringFields("itemId").isEqualTo(saved);
        });
    }

    @Test
    void addItem_WithNullOwnerIdTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);

        assertThrows(ValidationException.class,
                () -> itemService.createItem(forCreation, null));
    }

    @Test
    void addItem_WithUserNotFoundTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);

        assertThrows(NotFoundException.class,
                () -> itemService.createItem(forCreation, 573L));
    }

    @Test
    void addItem_WithRequestIdTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        RequestInputDto request = new RequestInputDto();
        request.setRequestDescription("Test request description");
        RequestOutputDto reqSaved = requestService.addItemRequest(request, user.getUserId());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        forCreation.setRequestId(reqSaved.getItemRequestId());

        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());
        assertAll(() -> {
            assertNotNull(saved);
            assertEquals(reqSaved.getItemRequestId(), saved.getRequestId());
        });
    }

    @Test
    void addItem_WithWrongRequestIdTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        forCreation.setRequestId(525L);

        assertThrows(NotFoundException.class,
                () -> itemService.createItem(forCreation, user.getUserId()));

    }

    @Test
    void getItemByIdTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        ItemResponseDtoWithComments found = itemService.getItemById(saved.getItemId(), user.getUserId());

        assertAll(() -> {
            assertNotNull(found);
            assertThat(found.getItemId()).isEqualTo(saved.getItemId());
        });
    }

    @Test
    void getItemById_WithWrongItemIdTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        ItemResponseDtoWithComments found = itemService.getItemById(saved.getItemId(), user.getUserId());

        assertThrows(NotFoundException.class,
                () -> itemService.getItemById(535L, user.getUserId()));
    }

    @Test
    void getItemById_WithOwnerIdTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        ItemResponseDtoWithComments found = itemService.getItemById(saved.getItemId(), user.getUserId());

        assertAll(() -> {
            assertNotNull(found);
            assertNotNull(found.getComments());
        });
    }

    @Test
    void getItemById_WithWrongOwnerIdTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        ItemResponseDtoWithComments found = itemService.getItemById(saved.getItemId(), 237L);
        assertAll(() -> {
            assertNotNull(found);
            assertEquals(0, found.getComments().size());
        });
    }

    @Test
    void getItemById_WithOwnerId_AndLastBookingTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        UserResponseDto user2 = userService.createUser(user2ForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 3, 7, 14, 40, 35));
        booking2.setEnd(LocalDateTime.of(2025, 5, 25, 18, 12, 43));
        booking2.setItemId(saved.getItemId());
        booking2.setBookerId(user2.getUserId());
        bookingService.addBooking(booking2, user2.getUserId());

        ItemResponseDtoWithComments found = itemService.getItemById(saved.getItemId(), user.getUserId());
        assertAll(() -> {
            assertNotNull(found);
            assertNotNull(found.getLastBooking());
        });
    }

    @Test
    void getItemById_WithOwnerId_AndNextBookingTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        UserResponseDto user2 = userService.createUser(user2ForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 9, 7, 14, 40, 35));
        booking2.setEnd(LocalDateTime.of(2025, 10, 25, 18, 12, 43));
        booking2.setItemId(saved.getItemId());
        booking2.setBookerId(user2.getUserId());
        bookingService.addBooking(booking2, user2.getUserId());

        ItemResponseDtoWithComments found = itemService.getItemById(saved.getItemId(), user.getUserId());
        assertAll(() -> {
            assertNotNull(found);
            assertNotNull(found.getNextBooking());
        });
    }

    @Test
    void updateItemTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        ItemRequestDto forUpdate = new ItemRequestDto();
        forUpdate.setItemName("TestName");
        forUpdate.setItemDescription("Test description");
        forUpdate.setIsAvailable(true);

        ItemResponseDto updated = itemService.updateItem(saved.getItemId(), forUpdate, user.getUserId());

        assertAll(() -> {
            assertNotNull(updated);
            assertThat(forUpdate).usingRecursiveComparison()
                    .ignoringFields("itemId", "requestId").isEqualTo(updated);
        });
    }

    @Test
    void updateItem_WithWrongOwnerTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        UserResponseDto user2 = userService.createUser(user2ForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        ItemRequestDto forUpdate = new ItemRequestDto();
        forUpdate.setItemName("TestName");
        forUpdate.setItemDescription("Test description");
        forUpdate.setIsAvailable(true);

        assertThrows(AnotherUserException.class,
                () -> itemService.updateItem(saved.getItemId(), forUpdate, user2.getUserId()));

    }

    @Test
    void updateItem_WithoutItemNameTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        ItemRequestDto forUpdate = new ItemRequestDto();
        forUpdate.setItemDescription("Test item description");
        forUpdate.setIsAvailable(true);

        ItemResponseDto updated = itemService.updateItem(saved.getItemId(), forUpdate, user.getUserId());
        assertAll(() -> {
            assertNotNull(updated);
            assertThat(forUpdate).usingRecursiveComparison()
                    .ignoringFields("itemId", "requestId", "itemName").isEqualTo(updated);
        });
    }

    @Test
    void updateItem_WithoutItemDescriptionTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        ItemRequestDto forUpdate = new ItemRequestDto();
        forUpdate.setItemName("Test");
        forUpdate.setIsAvailable(true);

        ItemResponseDto updated = itemService.updateItem(saved.getItemId(), forUpdate, user.getUserId());
        assertAll(() -> {
            assertNotNull(updated);
            assertThat(forUpdate).usingRecursiveComparison()
                    .ignoringFields("itemId", "requestId", "itemDescription").isEqualTo(updated);
        });
    }

    @Test
    void updateItem_WithoutIsAvailableTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        ItemRequestDto forUpdate = new ItemRequestDto();
        forUpdate.setItemName("Test");
        forUpdate.setItemDescription("Test item description");

        ItemResponseDto updated = itemService.updateItem(saved.getItemId(), forUpdate, user.getUserId());
        assertAll(() -> {
            assertNotNull(updated);
            assertThat(forUpdate).usingRecursiveComparison()
                    .ignoringFields("itemId", "requestId", "isAvailable").isEqualTo(updated);
        });
    }

    @Test
    void getAllItemsForOwnerTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        ItemRequestDto forCreation2 = new ItemRequestDto();
        forCreation2.setItemName("Test2");
        forCreation2.setItemDescription("Test item description2");
        forCreation2.setIsAvailable(true);
        ItemResponseDto saved2 = itemService.createItem(forCreation2, user.getUserId());

        Collection<ItemResponseDto> items = itemService.getAllItemsForOwner(user.getUserId());
        assertAll(() -> {
            assertNotNull(items);
            assertEquals(2, items.size());
        });
    }

    @Test
    void getAllItemsForOwner_WithWrongUserTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        ItemRequestDto forCreation2 = new ItemRequestDto();
        forCreation2.setItemName("Test2");
        forCreation2.setItemDescription("Test item description2");
        forCreation2.setIsAvailable(true);
        ItemResponseDto saved2 = itemService.createItem(forCreation2, user.getUserId());

        assertThrows(NotFoundException.class,
                () -> itemService.getAllItemsForOwner(458L));
    }

    @Test
    void addCommentTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        UserResponseDto user2 = userService.createUser(user2ForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 3, 7, 14, 40, 35));
        booking2.setEnd(LocalDateTime.of(2025, 5, 25, 18, 12, 43));
        booking2.setItemId(saved.getItemId());
        booking2.setBookerId(user2.getUserId());
        BookingResponseDto bookingSaved = bookingService.addBooking(booking2, user.getUserId());
        bookingService.bookingApprove(bookingSaved.getBookingId(), user.getUserId(), false);

        CommentRequestDto comment = new CommentRequestDto();
        comment.setText("Test comment");

        CommentResponseCreatedDto created = itemService.addComment(user.getUserId(), comment, saved.getItemId());

        assertAll(() -> {
            assertNotNull(created);
            assertEquals("Test comment", created.getText());
        });
    }

    @Test
    void addComment_WithWrongCommentatorTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        UserResponseDto user2 = userService.createUser(user2ForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 3, 7, 14, 40, 35));
        booking2.setEnd(LocalDateTime.of(2025, 5, 25, 18, 12, 43));
        booking2.setItemId(saved.getItemId());
        booking2.setBookerId(user2.getUserId());
        BookingResponseDto bookingSaved = bookingService.addBooking(booking2, user.getUserId());
        bookingService.bookingApprove(bookingSaved.getBookingId(), user.getUserId(), false);

        CommentRequestDto comment = new CommentRequestDto();
        comment.setText("Test comment");

        assertThrows(NotFoundException.class,
                () -> itemService.addComment(256L, comment, saved.getItemId()));
    }

    @Test
    void addComment_WithWrongDateTest() {
        UserResponseDto user = userService.createUser(userForCreate());
        UserResponseDto user2 = userService.createUser(user2ForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        BookingRequestDto booking2 = booking1ForCreate();
        booking2.setStart(LocalDateTime.of(2025, 6, 8, 14, 40, 35));
        booking2.setEnd(LocalDateTime.of(2025, 9, 25, 18, 12, 43));
        booking2.setItemId(saved.getItemId());
        booking2.setBookerId(user2.getUserId());
        BookingResponseDto bookingSaved = bookingService.addBooking(booking2, user.getUserId());
        bookingService.bookingApprove(bookingSaved.getBookingId(), user.getUserId(), false);

        CommentRequestDto comment = new CommentRequestDto();
        comment.setText("Test comment");

        assertThrows(ValidationException.class,
                () -> itemService.addComment(user.getUserId(), comment, saved.getItemId()));
    }

    @Test
    void searchByTextTest() {
        UserResponseDto user = userService.createUser(userForCreate());

        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        ItemResponseDto saved = itemService.createItem(forCreation, user.getUserId());

        Collection<ItemResponseDto> items = itemService.searchByText("Test");

        assertAll(() -> {
            assertNotNull(items);
            assertEquals(1, items.size());
        });
    }
}
