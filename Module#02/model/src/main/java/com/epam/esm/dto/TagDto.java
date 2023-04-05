package com.epam.esm.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class TagDto extends BaseDto {

    private Long id;
    private String name;
}
