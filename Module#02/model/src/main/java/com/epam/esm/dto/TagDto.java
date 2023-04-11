package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class TagDto implements Serializable {
    private Long id;
    private String name;

    @JsonCreator
    public TagDto(
            @JsonProperty("id") final Long id,
            @JsonProperty("name") final String name) {
        this.id = id;
        this.name = name;
    }
}
