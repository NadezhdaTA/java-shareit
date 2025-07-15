package ru.practicum.shareit.booking.dto;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum BookingState {
    // Все
    ALL("ALL"),
    // Текущие
    CURRENT("CURRENT"),
    // Будущие
    FUTURE("FUTURE"),
    // Завершенные
    PAST("PAST"),
    // Отклоненные
    REJECTED("REJECTED"),
    // Ожидающие подтверждения
    WAITING("WAITING"),;

    private final String value;

    BookingState(String value) {
        this.value = value;
    }

    public static Optional<BookingState> from(String stringState) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}