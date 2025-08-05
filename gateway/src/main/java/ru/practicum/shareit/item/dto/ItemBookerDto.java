package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemBookerDto {

    @JsonProperty("id")
    private Long itemId;

    @JsonProperty("name")
    private String itemName;
}
