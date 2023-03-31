package com.epam.esm.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class TagDto {

    private Long id;
    private String name;

}
