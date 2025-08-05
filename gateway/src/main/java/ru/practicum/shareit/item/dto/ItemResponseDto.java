package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {

    @NotNull
    @JsonProperty("id")
    private Long itemId;

    @JsonProperty("name")
    private String itemName;

    @JsonProperty("description")
    private String itemDescription;

    @JsonProperty("available")
    private Boolean isAvailable;

    @JsonProperty("userId")
    private Long ownerId;

    private Long requestId;
}
