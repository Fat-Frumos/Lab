package com.epam.esm.controller.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagAssembler
        implements RepresentationModelAssembler<TagDto, EntityModel<TagDto>> {
    /**
     * Converts the given entity into a {@code Dto},
     * which extends {@link RepresentationModelAssembler}.
     *
     * @param dto must not be {@literal null}.
     * @return {@code EntityModel<Dto>}
     */
    @NonNull
    @Override
    public EntityModel<TagDto> toModel(@NonNull final TagDto dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(TagController.class).getById(dto.getId())).withSelfRel(),
                linkTo(methodOn(TagController.class).delete(dto.getId())).withRel("delete")
        );
    }

    /**
     * Converts an {@link Iterable} or {@code Dto}s into an {@link Iterable}
     * of {@link RepresentationModelAssembler} and wraps them
     * in a {@link CollectionModel} instance.
     *
     * @param entities must not be {@literal null}.
     * @return {@link CollectionModel} containing {@code EntityModel<Dto>}.
     */
    @NonNull
    @Override
    public CollectionModel<EntityModel<TagDto>> toCollectionModel(
            final Iterable<? extends TagDto> entities) {
        return CollectionModel.of(StreamSupport
                        .stream(entities.spliterator(), false)
                        .map(this::toModel)
                        .collect(toList()),
                linkTo(methodOn(TagController.class)
                        .getAll(PageRequest.of(0, 25, Sort.by("id").ascending()))).withSelfRel());
    }
}
