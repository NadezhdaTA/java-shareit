package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.request.dto.RequestInputDto;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.request.dto.RequestOutputWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RequestMapper {
    ItemRequest toRequest(RequestInputDto itemRequestInputDto);

    RequestOutputDto toRequestOutputDto(ItemRequest itemRequest);

    RequestOutputWithItemsDto toRequestOutputWithItemsDto(ItemRequest itemRequest);
}
