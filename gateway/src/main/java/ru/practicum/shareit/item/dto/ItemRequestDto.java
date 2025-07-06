package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    @JsonProperty("id")
    private Long itemId;

    @NotEmpty
    @JsonProperty("name")
    private String itemName;

    @NotEmpty
    @Length(max = 150)
    @JsonProperty("description")
    private String itemDescription;

    @NotNull
    @JsonProperty("available")
    private Boolean isAvailable;

    private Long requestId;

}
