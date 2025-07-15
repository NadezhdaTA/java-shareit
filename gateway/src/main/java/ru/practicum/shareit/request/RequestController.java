package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestInputDto;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestClient requestService;
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestBody @Valid RequestInputDto requestInputDto,
                                       @RequestHeader(USER_ID) Long userId) {
        return requestService.addItemRequest(requestInputDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestByUser(@RequestHeader(USER_ID) Long userId) {
        return requestService.getRequestByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Long requestId) {
        return requestService.getRequestById(requestId);
    }
}

