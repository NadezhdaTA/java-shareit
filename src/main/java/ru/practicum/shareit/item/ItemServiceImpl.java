package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.intrfaces.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AnotherUserException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.interfaces.CommentRepository;
import ru.practicum.shareit.item.interfaces.ItemRepository;
import ru.practicum.shareit.item.interfaces.ItemServiceInterface;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.interfaces.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemServiceInterface {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;

    public ItemResponseDto createItem(ItemRequestDto item, Long ownerId) {
        if (ownerId == null) {
            throw new ValidationException("Owner id cannot be null");
        }

        User owner = userRepository.getUserByUserId(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " not found"));

        Item itemSaved = itemMapper.toItem(item);
        itemSaved.setOwner(owner);

        itemRepository.save(itemSaved);
        ItemResponseDto responseDto = itemMapper.toItemResponseDto(itemSaved);
        mapResponseDto(itemSaved, responseDto);

        return responseDto;
    }

    public ItemResponseDtoWithComments getItemById(Long id) {
         ItemResponseDto item = itemMapper.toItemResponseDto(itemRepository.getItemByItemId(id)
                .orElseThrow(() -> new NotFoundException("Item with id " + id + " not found")));

         List<CommentResponseDto> comments = commentRepository.getCommentsByItem_ItemId(id).stream()
                 .map(commentMapper::toCommentResponseDto)
                 .collect(Collectors.toList());

         ItemResponseDtoWithComments itemDto = itemMapper.toItemResponseDtoWithComments(
                 itemRepository.getItemByItemId(id).get());
         itemDto.setComments(comments);

        return itemDto;
    }

    public ItemResponseDto updateItem(Long itemId,
                                      ItemRequestDto item, Long ownerId) {

        User owner = userRepository.getUserByUserId(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " not found"));

        Item foundItem = itemRepository.getItemByItemId(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));

        if (foundItem.getOwner().getUserId().equals(ownerId)) {

            if (item.getIsAvailable() != null) {
                foundItem.setIsAvailable(item.getIsAvailable());
            }

            if (item.getItemName() != null) {
                foundItem.setItemName(item.getItemName());
            }

            if (item.getItemDescription() != null) {
                foundItem.setItemDescription(item.getItemDescription());
            }

        } else {
            throw new AnotherUserException("User with id " + ownerId + " is not owner of this Item");
        }

        ItemResponseDto responseDto = itemMapper.toItemResponseDto(foundItem);
        mapResponseDto(foundItem, responseDto);

        return responseDto;
    }

    public Collection<ItemResponseDto> getAllItemsForOwner(Long ownerId) {
        User owner = userRepository.getUserByUserId(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " not found"));

        return itemRepository.getItemsByOwner_UserId(ownerId).stream()
                .map(itemMapper::toItemResponseDto)
                .peek(item -> item.setOwnerId(ownerId))
                .collect(Collectors.toList());
    }

    public Collection<ItemResponseDto> searchByText(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.searchByTextContainingIgnoreCase(text).stream()
                .filter(Item::getIsAvailable)
                .map(itemMapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    public CommentResponseCreatedDto addComment(Long commentatorId,
                                                CommentRequestDto commentDto, Long itemId) {
        Collection<Booking> bookings = bookingRepository.getBookingsByItem_ItemId(itemId);

        Booking booking = bookings.stream()
                .filter(booking1 -> booking1.getBooker().getUserId().equals(commentatorId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Booking with bookerId " + commentatorId + " not found"));

        if (!booking.getEnd().isBefore(commentDto.getDate())) {
            throw new ValidationException("Booking end date cannot be before comment date");
        }

        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("Booking status cannot be APPROVED");
        }

        Comment comment = commentMapper.toComment(commentDto);
        comment.setItem(itemRepository.getItemByItemId(itemId).get());
        comment.setAuthor(userRepository.getUserByUserId(commentatorId).get());

        return commentMapper.toCommentResponseCreatedDto(
                commentRepository.save(comment));
    }

    private void mapResponseDto(Item item, ItemResponseDto itemResponseDto) {
        itemResponseDto.setOwnerId(item.getOwner().getUserId());

        if (item.getItemRequest() != null) {
            itemResponseDto.setRequestId(item.getItemRequest().getItemRequestId());
        }
    }

}

