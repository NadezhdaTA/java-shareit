package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;



@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    public static final String OWNER_ID = "X-Sharer-User-Id";
    private final ItemClient itemService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(OWNER_ID) Long ownerId,
                                  @RequestBody ItemRequestDto item) {
        return itemService.create(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemRequestDto item,
                                  @PathVariable Long itemId,
                                  @RequestHeader(OWNER_ID) Long ownerId) {
        return itemService.update(itemId, item, ownerId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId,
                                                   @RequestHeader(OWNER_ID) Long ownerId) {
        return itemService.getItemById(itemId, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsForOwner(
            @RequestHeader(OWNER_ID) Long ownerId) {
        return itemService.getAllItemsForOwner(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        return itemService.searchByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody CommentRequestDto comment,
                                                @PathVariable Long itemId,
                                                @RequestHeader(OWNER_ID) Long commentatorId) {
        return itemService.addComment(commentatorId, comment, itemId);
    }

}
