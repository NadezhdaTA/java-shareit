package ru.practicum.shareit.serviceTest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.AnotherUserException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Sql(value = "/test-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemServiceTest extends TestData {
    private final ItemServiceImpl itemService;

    @Test
    public void addItemTest() {
        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);

        ItemResponseDto saved = itemService.createItem(forCreation, user1.getUserId());

        assertAll(() -> {
            assertNotNull(saved);
            assertThat(forCreation).usingRecursiveComparison()
                    .ignoringFields("itemId").isEqualTo(saved);
        });
    }

    @Test
    void addItem_WithNullOwnerIdTest() {
        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);

        assertThrows(ValidationException.class,
                () -> itemService.createItem(forCreation, null));
    }

    @Test
    void addItem_WithUserNotFoundTest() {
        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);

        assertThrows(NotFoundException.class,
                () -> itemService.createItem(forCreation, 5L));
    }

    @Test
    void addItem_WithRequestIdTest() {
        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        forCreation.setRequestId(1L);

        ItemResponseDto saved = itemService.createItem(forCreation, user1.getUserId());
        assertAll(() -> {
            assertNotNull(saved);
            assertEquals(1L, saved.getRequestId());
        });
    }

    @Test
    void addItem_WithWrongRequestIdTest() {
        ItemRequestDto forCreation = new ItemRequestDto();
        forCreation.setItemName("Test");
        forCreation.setItemDescription("Test item description");
        forCreation.setIsAvailable(true);
        forCreation.setRequestId(5L);

        assertThrows(NotFoundException.class,
                () -> itemService.createItem(forCreation, user1.getUserId()));

    }

    @Test
    void getItemByIdTest() {
        ItemResponseDtoWithComments found = itemService.getItemById(1L, 1L);

        assertAll(() -> {
            assertNotNull(found);
            assertThat(found.getItemId()).isEqualTo(1L);
        });
    }

    @Test
    void getItemById_WithWrongItemIdTest() {
        assertThrows(NotFoundException.class,
                () -> itemService.getItemById(5L, 2L));
    }

    @Test
    void getItemById_WithOwnerIdTest() {
        ItemResponseDtoWithComments found = itemService.getItemById(1L, 1L);
        assertAll(() -> {
            assertNotNull(found);
            assertNotNull(found.getComments());
        });
    }

    @Test
    void getItemById_WithNotOwnerIdTest() {
        ItemResponseDtoWithComments found = itemService.getItemById(2L, 1L);
        assertAll(() -> {
            assertNotNull(found);
            assertEquals(0, found.getComments().size());
        });
    }

    @Test
    void getItemById_WithOwnerId_AndLastBookingTest() {
        ItemResponseDtoWithComments found = itemService.getItemById(1L, 1L);
        assertAll(() -> {
            assertNotNull(found);
            assertNotNull(found.getLastBooking());
        });
    }

    @Test
    void getItemById_WithOwnerId_AndNextBookingTest() {
        ItemResponseDtoWithComments found = itemService.getItemById(1L, 1L);
        assertAll(() -> {
            assertNotNull(found);
            assertNotNull(found.getNextBooking());
        });
    }

    @Test
    void updateItemTest() {
        ItemRequestDto forUpdate = new ItemRequestDto();
        forUpdate.setItemName("Test");
        forUpdate.setItemDescription("Test item description");
        forUpdate.setIsAvailable(true);

        ItemResponseDto updated = itemService.updateItem(1L, forUpdate, user1.getUserId());
        assertAll(() -> {
            assertNotNull(updated);
            assertThat(forUpdate).usingRecursiveComparison()
                    .ignoringFields("itemId", "requestId").isEqualTo(updated);
        });
    }

    @Test
    void updateItem_WithWrongOwnerTest() {
        ItemRequestDto forUpdate = new ItemRequestDto();
        forUpdate.setItemName("Test");
        forUpdate.setItemDescription("Test item description");
        forUpdate.setIsAvailable(true);

        assertThrows(AnotherUserException.class,
                () -> itemService.updateItem(1L, forUpdate, user2.getUserId()));

    }

    @Test
    void updateItem_WithoutItemNameTest() {
        ItemRequestDto forUpdate = new ItemRequestDto();
        forUpdate.setItemDescription("Test item description");
        forUpdate.setIsAvailable(true);

        ItemResponseDto updated = itemService.updateItem(1L, forUpdate, user1.getUserId());
        assertAll(() -> {
            assertNotNull(updated);
            assertThat(forUpdate).usingRecursiveComparison()
                    .ignoringFields("itemId", "requestId", "itemName").isEqualTo(updated);
        });
    }

    @Test
    void updateItem_WithoutItemDescriptionTest() {
        ItemRequestDto forUpdate = new ItemRequestDto();
        forUpdate.setItemName("Test");
        forUpdate.setIsAvailable(true);

        ItemResponseDto updated = itemService.updateItem(1L, forUpdate, user1.getUserId());
        assertAll(() -> {
            assertNotNull(updated);
            assertThat(forUpdate).usingRecursiveComparison()
                    .ignoringFields("itemId", "requestId", "itemDescription").isEqualTo(updated);
        });
    }

    @Test
    void updateItem_WithoutIsAvailableTest() {
        ItemRequestDto forUpdate = new ItemRequestDto();
        forUpdate.setItemName("Test");
        forUpdate.setItemDescription("Test item description");

        ItemResponseDto updated = itemService.updateItem(1L, forUpdate, user1.getUserId());
        assertAll(() -> {
            assertNotNull(updated);
            assertThat(forUpdate).usingRecursiveComparison()
                    .ignoringFields("itemId", "requestId", "isAvailable").isEqualTo(updated);
        });
    }

    @Test
    void getAllItemsForOwnerTest() {
        Collection<ItemResponseDto> items = itemService.getAllItemsForOwner(user1.getUserId());
        assertAll(() -> {
            assertNotNull(items);
            assertEquals(1, items.size());
        });
    }

    @Test
    void getAllItemsForOwner_WithWrongUserTest() {
        assertThrows(NotFoundException.class,
                () -> itemService.getAllItemsForOwner(5L));
    }

    @Test
    void addCommentTest() {
        CommentRequestDto comment = new CommentRequestDto();
        comment.setText("Test comment");

        CommentResponseCreatedDto created = itemService.addComment(1L, comment, 2L);

        assertAll(() -> {
            assertNotNull(created);
            assertEquals("Test comment", created.getText());
        });
    }

    @Test
    void addComment_WithWrongCommentatorTest() {
        CommentRequestDto comment = new CommentRequestDto();
        comment.setText("Test comment");

        assertThrows(NotFoundException.class,
                () -> itemService.addComment(5L, comment, 2L));
    }

    @Test
    void addComment_WithWrongDateTest() {
        CommentRequestDto comment = new CommentRequestDto();
        comment.setText("Test comment");

        assertThrows(ValidationException.class,
                () -> itemService.addComment(1L, comment, 1L));
    }

    @Test
    void searchByTextTest() {
        Collection<ItemResponseDto> items = itemService.searchByText("testItem2");

        assertAll(() -> {
            assertNotNull(items);
            assertEquals(1, items.size());
        });
    }
}
