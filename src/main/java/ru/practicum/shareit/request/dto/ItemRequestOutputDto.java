package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestOutputDto {
    private Long itemRequestId;
    private String requestDescription;
    private Long requesterId;
    private LocalDate requestDate;
}
