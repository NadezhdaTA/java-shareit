package ru.practicum.shareit.item;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    public static final String OWNER_ID = "X-Sharer-User-Id";
    private final ItemServiceImpl itemService;

    @PostMapping
    public ItemResponseDto create(@RequestHeader(OWNER_ID) Long ownerId,
                                  @Valid @RequestBody ItemRequestDto item) {
        return itemService.createItem(item, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@RequestBody ItemRequestDto item,
                                  @PathVariable Long itemId,
                                  @RequestHeader(OWNER_ID) Long ownerId) {
        return itemService.updateItem(itemId, item, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDtoWithComments getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public Collection<ItemResponseDto> getAllItemsForOwner(
            @RequestHeader(OWNER_ID) Long ownerId) {
        return itemService.getAllItemsForOwner(ownerId);
    }

    @GetMapping("/search")
    public Collection<ItemResponseDto> searchItems(@RequestParam String text) {
        return itemService.searchByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseCreatedDto addComment(@RequestBody CommentRequestDto comment,
                                                @PathVariable Long itemId,
                                                @RequestHeader(OWNER_ID) Long commentatorId) {
        return itemService.addComment(commentatorId, comment, itemId);
    }

}
