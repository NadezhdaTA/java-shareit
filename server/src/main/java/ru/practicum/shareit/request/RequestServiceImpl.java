package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.interfaces.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.RequestInputDto;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.request.dto.RequestOutputWithItemsDto;
import ru.practicum.shareit.request.interfaces.RequestServiceInterface;
import ru.practicum.shareit.request.interfaces.RequestRepository;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.interfaces.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestServiceInterface {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final RequestMapper requestMapper;
    private final ItemMapper itemMapper;

    @Override
    public RequestOutputDto addItemRequest(RequestInputDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        ItemRequest itemRequest = requestMapper.toRequest(requestDto);
        itemRequest.setRequester(user);
        return requestMapper.toRequestOutputDto(requestRepository.save(itemRequest));
    }

    @Override
    public List<RequestOutputDto> getRequestByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return requestRepository.findItemRequestByRequester_UserIdOrderByCreatedAtDesc(userId).stream()
                .map(requestMapper::toRequestOutputDto)
                .toList();
    }

    @Override
    public List<RequestOutputDto> getAllRequests() {
        return requestRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(requestMapper::toRequestOutputDto)
                .toList();
    }

    @Override
    public RequestOutputWithItemsDto getRequestById(Long requestId) {
        ItemRequest itemRequest = requestRepository.getRequestByItemRequestId(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        Collection<ItemResponseDto> items =
                itemRepository.getItemsByItemRequest_ItemRequestId(requestId).stream()
                .map(itemMapper::toItemResponseDto)
                .toList();

        RequestOutputWithItemsDto responseDto = requestMapper.toRequestOutputWithItemsDto(itemRequest);
        responseDto.setItems(items);

        return responseDto;
    }


}
