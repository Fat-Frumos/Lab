package com.epam.esm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDto {
    @NotNull(message = "Id cannot be blank")
    private Long id;
    @Size(min = 1, max = 128)
    @NotNull(message = "Name cannot be blank")
    private String username;
    @Size(min = 1, max = 128)
    @NotNull(message = "Email cannot be blank")
    private String email;
    private Set<OrderDto> orderDtos;
}
