package com.epam.esm.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDto implements Linkable {
    private Long id;
    private String username;
    private String email;
    private Set<OrderDto> orderDtos;
}
