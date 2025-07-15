package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestInputDto;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.request.dto.RequestOutputWithItemsDto;
import ru.practicum.shareit.request.interfaces.RequestServiceInterface;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestServiceInterface requestService;
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public RequestOutputDto addRequest(@RequestBody RequestInputDto requestInputDto,
                                       @RequestHeader(USER_ID) Long userId) {
        return requestService.addItemRequest(requestInputDto, userId);
    }

    @GetMapping
    public Collection<RequestOutputDto> getRequestByUser(@RequestHeader(USER_ID) Long userId) {
        return requestService.getRequestByUser(userId);
    }

    @GetMapping("/all")
    public List<RequestOutputDto> getAllRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public RequestOutputWithItemsDto getRequestById(@PathVariable Long requestId) {
        return requestService.getRequestById(requestId);
    }
}

