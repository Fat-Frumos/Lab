package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * A DTO (Data Transfer Object) representing a Tag entity,
 * used for transferring data between layers of the application.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class TagDto implements Serializable {

    /**
     * The unique identifier of the tag.
     */
    private Long id;

    /**
     * The name of the tag.
     */
    @NotBlank(message = "Name cannot be blank")
    private String name;

    /**
     * Constructs a new TagDto object with the given id and name.
     *
     * @param id   the unique identifier of the tag.
     * @param name the name of the tag.
     */
    @JsonCreator
    public TagDto(
            @JsonProperty("id") final Long id,
            @JsonProperty("name") final String name) {
        this.id = id;
        this.name = name;
    }
}
