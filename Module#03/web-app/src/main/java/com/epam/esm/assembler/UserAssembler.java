package com.epam.esm.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.criteria.FilterParams;
import com.epam.esm.dto.Linkable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import javax.swing.SortOrder;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class UserAssembler implements RepresentationModelAssembler<Linkable, EntityModel<Linkable>> {

    /**
     * Converts the given entity into a {@code D},
     * which extends {@link RepresentationModelAssembler}.
     */
    @NonNull
    @Override
    public EntityModel<Linkable> toModel(
            @NonNull Linkable userDto) {
        return EntityModel.of(userDto,
                linkTo(methodOn(UserController.class).getUser(userDto.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrdersByUserId(userDto.getId())).withRel("orders"),
                linkTo(methodOn(UserController.class).getUserCertificates(userDto.getId())).withRel("certificates")
        );
    }

    /**
     * Converts an {@link Iterable} or {@code T}s
     * into an {@link Iterable} of {@link RepresentationModelAssembler}
     * and wraps them in a {@link CollectionModel} instance.
     *
     * @param users must not be {@literal null}.
     * @return {@link CollectionModel} containing {@code D}.
     */
    @NonNull
    @Override
    public CollectionModel<EntityModel<Linkable>> toCollectionModel(
            @NonNull Iterable<? extends Linkable> users) {
        return CollectionModel.of(StreamSupport
                        .stream(users.spliterator(), false)
                        .map(this::toModel)
                        .collect(toList()),
                linkTo(methodOn(UserController.class)
                        .getUsers(SortOrder.UNSORTED, FilterParams.ID, 0, 25)).withSelfRel());
    }
}
