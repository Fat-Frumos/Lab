package com.epam.esm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data transfer object for representing a slim version of a user.
 * Inherits from the RepresentationModel class for HATEOAS support.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class UserSlimDto extends RepresentationModel<UserSlimDto> {
    /**
     * The ID of the user.
     */
    private Long id;
    /**
     * The username of the user.
     */
    @Size(min = 1, max = 128)
    @NotNull(message = "Name cannot be blank")
    private String username;

    /**
     * The email of the user.
     */
    @Size(min = 1, max = 128)
    private String email;
}
