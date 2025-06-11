package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @JsonProperty("id")
    private Long userId;

    @NotBlank
    @JsonProperty("name")
    private String userName;

    @NotNull
    @Email
    @JsonProperty("email")
    private String userEmail;
}
