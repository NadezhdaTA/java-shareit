package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {
    ItemResponseDto toItemResponseDto(ItemRequestDto itemRequestDto);

    ItemRequestDto toItemRequestDto(ItemResponseDto itemDtoResponseDto);

    Item toItem(ItemRequestDto itemRequestDto);

    ItemResponseDto toItemResponseDto(Item item);

}
