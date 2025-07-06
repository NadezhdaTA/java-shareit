package ru.practicum.shareit.request.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ItemRequest,Long> {
    ItemRequest save(ItemRequest item);

    List<ItemRequest> getRequestByRequester_UserId(Long userId);

    List<ItemRequest> findAll();

    Optional<ItemRequest> getRequestByItemRequestId(Long requestId);
}
