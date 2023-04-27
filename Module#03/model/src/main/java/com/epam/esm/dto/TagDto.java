package com.epam.esm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TagDto implements Serializable {

    @NotNull(message = "Id cannot be blank")
    private Long tagId;

    @NotNull(message = "Name cannot be blank")
    private String name;
}
