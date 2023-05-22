package com.epam.esm.controller;

import com.epam.esm.controller.assembler.UserAssembler;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserAssembler assembler;

    private final UserService userService;

    @GetMapping("/{id}")
    public EntityModel<UserDto> getUser(
            @PathVariable final Long id) {
        return assembler.toModel(
                userService.getById(id));
    }

    @GetMapping
    public CollectionModel<EntityModel<UserDto>> getUsers(
            @PageableDefault(size = 25, sort = {"id"},
                    direction = Sort.Direction.ASC)
            final Pageable pageable) {
        return assembler.toCollectionModel(
                userService.getAll(pageable));
    }
}
