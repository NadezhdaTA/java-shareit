package ru.practicum.shareit.item.interfaces;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDtoWithComments;

import java.util.Collection;

public interface ItemServiceInterface {
    ItemResponseDto createItem(ItemRequestDto item, Long ownerId);

    ItemResponseDtoWithComments getItemById(Long id, Long ownerId);

    ItemResponseDto updateItem(Long itemId, ItemRequestDto item, Long ownerId);

    Collection<ItemResponseDto> getAllItemsForOwner(Long ownerId);

    Collection<ItemResponseDto> searchByText(String text);

}
