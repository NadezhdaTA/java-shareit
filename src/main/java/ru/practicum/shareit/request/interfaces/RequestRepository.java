package ru.practicum.shareit.request.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;

public interface RequestRepository extends JpaRepository<ItemRequest,Long> {
    ItemRequest save(ItemRequest item);
}
