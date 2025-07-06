package ru.practicum.shareit.mapperTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ItemMapperTest {

    @Autowired
    private ItemMapper itemMapper;

    User user = new User(1L, "testName1", "test1@test.com");

    ItemRequest itemRequest = new ItemRequest(1L, "testDescription1",
            user, LocalDateTime.of(2025, 6, 25, 15, 25));

    @Test
    public void itemRequestDtoToItemTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                1L, "TestName", "Test description", true, 1L);

        Item item = itemMapper.toItem(itemRequestDto);

        assertAll(() -> {
            assertEquals(1L, item.getItemId());
            assertEquals("TestName", item.getItemName());
            assertEquals("Test description", item.getItemDescription());
            assertEquals(true, item.getIsAvailable());
            assertNull(item.getOwner());
        });
    }

    @Test
    public void itemToItemResponseDtoTest() {
        Item item = new Item(2L, "TestName2", "Test description2", true, user, itemRequest);

        ItemResponseDto itemResponseDto = itemMapper.toItemResponseDto(item);
        assertAll(() -> {
            assertEquals(2L, itemResponseDto.getItemId());
            assertEquals("TestName2", itemResponseDto.getItemName());
            assertEquals("Test description2", itemResponseDto.getItemDescription());
            assertEquals(true, itemResponseDto.getIsAvailable());
            assertEquals(user.getUserId(), itemResponseDto.getOwnerId());
            assertEquals(itemRequest.getItemRequestId(), itemResponseDto.getRequestId());
        });
    }

    @Test
    public void itemRequestDtoToItemResponseDtoTest() {
        ItemRequestDto item = new ItemRequestDto(
                3L, "TestName3", "Test description3", false, 3L);

        ItemResponseDto itemResponseDto = itemMapper.toItemResponseDto(item);
        assertAll(() -> {
            assertEquals(3L, itemResponseDto.getItemId());
            assertEquals("TestName3", itemResponseDto.getItemName());
            assertEquals("Test description3", itemResponseDto.getItemDescription());
            assertEquals(false, itemResponseDto.getIsAvailable());
            assertNull(itemResponseDto.getOwnerId());
            assertEquals(3L, itemResponseDto.getRequestId());
        });
    }

    @Test
    public void itemResponseDtoToItemRequestDtoTest() {
        ItemResponseDto itemResponseDto = new ItemResponseDto(
                4L, "TestName4", "Test description4",
                false, 4L, 4L);
        ItemRequestDto itemRequestDto = itemMapper.toItemRequestDto(itemResponseDto);
        assertAll(() -> {
            assertEquals(4L, itemRequestDto.getItemId());
            assertEquals("TestName4", itemRequestDto.getItemName());
            assertEquals("Test description4", itemRequestDto.getItemDescription());
            assertEquals(false, itemRequestDto.getIsAvailable());
            assertEquals(4L, itemRequestDto.getRequestId());
        });
    }

    @Test
    public void itemToItemResponseDtoWithCommentsTest() {

        Item item = new Item(2L, "testName2", "testDescription2",
                true, user, itemRequest);

        ItemResponseDtoWithComments itemResponse = itemMapper.toItemResponseDtoWithComments(item);

        assertAll(() -> {
            assertEquals(2L, itemResponse.getItemId());
            assertEquals("testName2", itemResponse.getItemName());
            assertEquals("testDescription2", itemResponse.getItemDescription());
            assertEquals(true, itemResponse.getIsAvailable());
            assertEquals(user.getUserId(), itemResponse.getOwnerId());
            assertEquals(itemRequest.getItemRequestId(), itemResponse.getRequestId());
            assertNull(itemResponse.getComments());
            assertNull(itemResponse.getLastBooking());
            assertNull(itemResponse.getNextBooking());
        });
    }

    @Test
    public void itemToItemBookerDto() {
        Item item = new Item(2L, "testName2", "testDescription2",
                true, user, itemRequest);

        ItemBookerDto itemBookerDto = itemMapper.toItemBookerDto(item);

        assertAll(() -> {
            assertEquals(2L, itemBookerDto.getItemId());
            assertEquals("testName2", itemBookerDto.getItemName());
        });
    }
}
