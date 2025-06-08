package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AnotherUserException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.interfaces.ItemServiceInterface;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService implements ItemServiceInterface {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Qualifier("itemMapper")
    private final ItemMapper itemMapper;

    public ItemResponseDto createItem(ItemRequestDto item, Long ownerId) {
        if (ownerId == null) {
            throw new ValidationException("Owner id cannot be null");
        }
        userStorage.getUserById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " not found"));
        Item itemSaved = itemMapper.toItem(item);
        itemSaved.setOwnerId(ownerId);
        return itemMapper.toItemResponseDto(itemStorage.createItem(itemSaved));
    }

    public ItemResponseDto getItemById(Long id) {
        return itemMapper.toItemResponseDto(itemStorage.getItemById(id)
                .orElseThrow(() -> new NotFoundException("Item with id " + id + " not found")));
    }

    public ItemResponseDto updateItem(Long itemId,
                                      ItemRequestDto item, Long ownerId) {
        findUserById(ownerId);
        Item foundItem = itemStorage.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
        if (foundItem.getOwnerId().equals(ownerId)) {
            item.setItemId(itemId);
            foundItem = itemMapper.toItem(item);
        } else {
            throw new AnotherUserException("User with id " + ownerId + " is not owner of this Item");
        }
        return itemMapper.toItemResponseDto(itemStorage.updateItem(foundItem));
    }

    public Collection<ItemResponseDto> getAllItemsForOwner(Long ownerId) {
        return itemStorage.getAllItemsForOwner(ownerId).stream()
                .map(itemMapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    public Collection<ItemResponseDto> searchByText(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemStorage.searchByText(text).stream()
                .filter(Item::getIsAvailable)
                .map(itemMapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    private void findUserById(Long userId) {
        userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found"));
    }
}
