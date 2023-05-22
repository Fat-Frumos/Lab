package com.epam.esm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagDto {
    @NotNull(message = "Id cannot be blank")
    private Long id;
    @Size(min = 1, max = 128)
    @NotNull(message = "Name cannot be blank")
    private String name;
}
