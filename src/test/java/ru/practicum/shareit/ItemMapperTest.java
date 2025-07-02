package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ItemMapperTest {

    @Autowired
    private ItemMapper itemMapper;

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
            assertNull(item.getOwnerId());
            assertEquals(1L, item.getRequestId());
        });
    }

    @Test
    public void itemToItemResponseDtoTest() {
        Item item = new Item(2L, "TestName2", "Test description2", true, 2L, 2L);

        ItemResponseDto itemResponseDto = itemMapper.toItemResponseDto(item);
        assertAll(() -> {
            assertEquals(2L, itemResponseDto.getItemId());
            assertEquals("TestName2", itemResponseDto.getItemName());
            assertEquals("Test description2", itemResponseDto.getItemDescription());
            assertEquals(true, itemResponseDto.getIsAvailable());
            assertEquals(2L, itemResponseDto.getOwnerId());
            assertEquals(2L, itemResponseDto.getRequestId());
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
                4L, "TestName4", "Test description4", false, 4L, 4L);
        ItemRequestDto itemRequestDto = itemMapper.toItemRequestDto(itemResponseDto);
        assertAll(() -> {
            assertEquals(4L, itemRequestDto.getItemId());
            assertEquals("TestName4", itemRequestDto.getItemName());
            assertEquals("Test description4", itemRequestDto.getItemDescription());
            assertEquals(false, itemRequestDto.getIsAvailable());
            assertEquals(4L, itemRequestDto.getRequestId());
        });
    }

}
