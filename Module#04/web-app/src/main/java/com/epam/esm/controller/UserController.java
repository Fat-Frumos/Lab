package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for managing users.
 * <p>
 * Provides REST endpoints to retrieve user information.
 * <p>
 * This class is annotated with {@link RestController}
 * to indicate that it is a Spring MVC controller,
 * and {@link RequestMapping} with a path of "/users"
 * to map requests to this controller.
 */
@RestController
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(path = "/users")
public class UserController {
    /**
     * The User Assembler used to convert User entities to User DTOs.
     */
    private final UserAssembler assembler;
    /**
     * The User Service used to retrieve and manipulate user data.
     */
    private final UserService userService;

    /**
     * Get a user by ID.
     *
     * @param id the user ID
     * @return the user DTO
     */
    @GetMapping("/{id}")
    public EntityModel<UserDto> getUser(
            @PathVariable final Long id) {
        return assembler.toModel(
                userService.getById(id));
    }

    /**
     * Get all users with pagination.
     *
     * @param pageable the pageable information
     * @return the collection of user DTOs
     */
    @GetMapping
    public CollectionModel<EntityModel<UserDto>> getUsers(
            @PageableDefault(size = 25, sort = {"id"},
                    direction = Sort.Direction.ASC) final Pageable pageable) {
        return assembler.toCollectionModel(
                userService.getAll(pageable));
    }
}
