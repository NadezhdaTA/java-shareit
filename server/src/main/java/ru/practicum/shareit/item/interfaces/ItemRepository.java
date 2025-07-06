package ru.practicum.shareit.item.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item save(Item item);

    Optional<Item> getItemByItemId(Long itemId);

    Collection<Item> getItemsByOwner_UserId(Long ownerUserId);

    @Query(value = "SELECT i FROM Item i WHERE upper(i.itemName) like upper(concat('%', ?1, '%')) " +
            " OR upper(i.itemDescription) LIKE upper(concat('%', ?1, '%'))")
    Collection<Item> searchByTextContainingIgnoreCase(String text);

    Collection<Item> getItemsByItemRequest_ItemRequestId(Long itemRequestId);
}
