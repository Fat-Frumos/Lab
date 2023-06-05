package com.epam.esm.assembler;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderDto;
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

/**
 * This class is an implementation
 * of the {@link RepresentationModelAssembler} interface that converts entities
 * of type {@link OrderDto} into {@link EntityModel} of {@link OrderDto}.
 */
@Component
public class OrderAssembler implements
        RepresentationModelAssembler<OrderDto, EntityModel<OrderDto>> {
    /**
     * Converts the given {@link OrderDto} entity
     * into an {@link EntityModel<OrderDto>}.
     *
     * @param dto the {@link OrderDto} entity to be converted
     * @return the {@link EntityModel<OrderDto>} representing the converted entity
     */
    @NonNull
    @Override
    public EntityModel<OrderDto> toModel(
            @NonNull final OrderDto dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(OrderController.class).getOrderById(dto.getId())).withSelfRel(),
                linkTo(methodOn(CertificateController.class).getAllByOrderId(dto.getId())).withRel("certificates"));
    }

    /**
     * Converts an {@link Iterable} of {@link OrderDto} entities
     * into a {@link CollectionModel} of {@link EntityModel}.
     *
     * @param entities the {@link OrderDto} entities to be converted
     * @return the {@link CollectionModel} of {@link EntityModel<OrderDto>}
     * representing the converted entities
     */
    @NonNull
    @Override
    public CollectionModel<EntityModel<OrderDto>> toCollectionModel(
            final Iterable<? extends OrderDto> entities) {
        return CollectionModel.of(StreamSupport
                        .stream(entities.spliterator(), false)
                        .map(this::toModel)
                        .collect(toList()),
                linkTo(methodOn(OrderController.class)
                        .getAllOrders(PageRequest.of(
                                0, 25, Sort.by("name")
                                        .ascending())))
                        .withSelfRel());
    }
}
