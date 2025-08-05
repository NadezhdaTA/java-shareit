package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    @NotNull
    @JsonProperty("id")
    private Long userId;

    @JsonProperty("name")
    private String userName;

    @JsonProperty("email")
    private String userEmail;
}
