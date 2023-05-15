package com.epam.esm.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class UserAssembler implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

    /**
     * Converts the given entity into a {@code D},
     * which extends {@link RepresentationModelAssembler}.
     */
    @NonNull
    @Override
    public EntityModel<UserDto> toModel(@NonNull UserDto userDto) {
        return EntityModel.of(userDto,
                linkTo(methodOn(UserController.class).getUser(userDto.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrdersByUserId(userDto.getId())).withRel("orders"),
                linkTo(methodOn(UserController.class).getUserCertificates(userDto.getId())).withRel("certificates"),
                linkTo(methodOn(OrderController.class).getAllOrdersByUserId(userDto.getId())).withRel("tag")
        );
    }

    /**
     * Converts an {@link Iterable} or {@code T}s into an {@link Iterable} of {@link RepresentationModelAssembler}
     * and wraps them in a {@link CollectionModel} instance.
     *
     * @param users must not be {@literal null}.
     * @return {@link CollectionModel} containing {@code D}.
     */
    @NonNull
    @Override
    public CollectionModel<EntityModel<UserDto>> toCollectionModel(@NonNull Iterable<? extends UserDto> users) {
        return CollectionModel.of(StreamSupport
                .stream(users.spliterator(), false)
                .map(this::toModel)
                .collect(toList()), linkTo(methodOn(UserController.class)
                .getUsers()).withSelfRel());
    }
}
