package ru.practicum.shareit.item.interfaces;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorageInterface {
    Item createItem(Item item);

    Optional<Item> getItemById(Long id);

    Item updateItem(Item item);

    Collection<Item> getAllItemsForOwner(Long ownerId);

    Collection<Item> searchByText(String text);

}
