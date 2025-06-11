package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.interfaces.ItemStorageInterface;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemStorage implements ItemStorageInterface {

    private Long nextItemId = 1L;
    private final Map<Long, Item> items = new HashMap<>();

    public Item createItem(Item item) {
        item.setItemId(nextItemId++);
        items.put(item.getItemId(), item);
        return item;
    }

    public Optional<Item> getItemById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    public Item updateItem(Item item) {
        Item oldItem = items.get(item.getItemId());

        if (oldItem != null) {

            if (item.getItemName() != null) {
                oldItem.setItemName(item.getItemName());
            }

            if (item.getItemDescription() != null) {
                oldItem.setItemDescription(item.getItemDescription());
            }

            if (item.getIsAvailable() != null) {
                oldItem.setIsAvailable(item.getIsAvailable());
            }

        }
        return oldItem;
    }

    public Collection<Item> getAllItemsForOwner(Long ownerId) {
        return items.values()
                .stream()
                .filter(item -> Objects.equals(item.getOwnerId(), ownerId))
                .collect(Collectors.toList());
    }

    public Collection<Item> searchByText(String text) {
        Collection<Item> itemCollection = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
                itemCollection.add(item);
            } else if (item.getItemDescription().toLowerCase().contains(text.toLowerCase())) {
                itemCollection.add(item);
            }
        }
        return itemCollection;
    }
}
