package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    @JsonProperty("id")
    private Long itemId;

    @JsonProperty("name")
    private String itemName;

    @JsonProperty("description")
    private String itemDescription;

    @JsonProperty("available")
    private Boolean isAvailable;

    private Long requestId;

}
