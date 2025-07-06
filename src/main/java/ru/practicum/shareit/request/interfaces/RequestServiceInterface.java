package ru.practicum.shareit.request.interfaces;

import ru.practicum.shareit.request.dto.RequestInputDto;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.request.dto.RequestOutputWithItemsDto;

import java.util.List;

public interface RequestServiceInterface {
    RequestOutputDto addItemRequest(RequestInputDto requestDto, Long userId);

    List<RequestOutputDto> getRequestByUser(Long userId);

    List<RequestOutputDto> getAllRequests();

    RequestOutputWithItemsDto getRequestById(Long requestId);
}
