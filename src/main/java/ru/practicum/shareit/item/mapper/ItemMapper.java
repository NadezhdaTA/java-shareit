package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.ItemBookerDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDtoWithComments;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {
    ItemResponseDto toItemResponseDto(ItemRequestDto itemRequestDto);

    ItemRequestDto toItemRequestDto(ItemResponseDto itemDtoResponseDto);

    Item toItem(ItemRequestDto itemRequestDto);

    default ItemResponseDto toItemResponseDto(Item item) {
        if (item == null) {
            return null;
        }

        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setItemId(item.getItemId());
        itemResponseDto.setItemName(item.getItemName());
        itemResponseDto.setItemDescription(item.getItemDescription());
        itemResponseDto.setIsAvailable(item.getIsAvailable());
        itemResponseDto.setOwnerId(item.getOwner().getUserId());

        if(item.getItemRequest() != null) {
            itemResponseDto.setRequestId(item.getItemRequest().getRequesterId());
        }

        return itemResponseDto;

    }

    ItemBookerDto toItemBookerDto(Item item);

    default ItemResponseDtoWithComments toItemResponseDtoWithComments(Item item) {
        if (item == null) {
            return null;
        }

        ItemResponseDtoWithComments itemResponseDtoWithComments = new ItemResponseDtoWithComments();

        itemResponseDtoWithComments.setItemId(item.getItemId());
        itemResponseDtoWithComments.setItemName(item.getItemName());
        itemResponseDtoWithComments.setItemDescription(item.getItemDescription());
        itemResponseDtoWithComments.setIsAvailable(item.getIsAvailable());
        itemResponseDtoWithComments.setItemId(item.getItemId());
        itemResponseDtoWithComments.setOwnerId(item.getOwner().getUserId());

        if(item.getItemRequest() != null) {
            itemResponseDtoWithComments.setRequestId(item.getItemRequest().getItemRequestId());
        }

        return itemResponseDtoWithComments;

    }

}
