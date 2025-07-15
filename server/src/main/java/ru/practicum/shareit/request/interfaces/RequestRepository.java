package ru.practicum.shareit.request.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ItemRequest,Long> {
    ItemRequest save(ItemRequest item);

    List<ItemRequest> findItemRequestByRequester_UserIdOrderByCreatedAtDesc(Long userId);

    List<ItemRequest> findAllByOrderByCreatedAtDesc();

    Optional<ItemRequest> getRequestByItemRequestId(Long requestId);

}
