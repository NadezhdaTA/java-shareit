package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private Long itemId;
    private String itemName;
    private String itemDescription;
    private Boolean isAvailable;
    private Long ownerId;
    private Long requestId;

}
