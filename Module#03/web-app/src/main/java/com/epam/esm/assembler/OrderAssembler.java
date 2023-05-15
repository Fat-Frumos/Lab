package com.epam.esm.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements RepresentationModelAssembler<OrderDto, EntityModel<OrderDto>> {
    /**
     * Converts the given entity into a {@code D}, which extends {@link RepresentationModelAssembler}.
     *
     * @param orderDto
     * @return
     */
    @Override
    public EntityModel<OrderDto> toModel(OrderDto orderDto) {
        return EntityModel.of(orderDto,
                linkTo(methodOn(OrderController.class).getOrderById(orderDto.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders"));
    }

    /**
     * Converts an {@link Iterable} or {@code T}s into an {@link Iterable} of {@link RepresentationModelAssembler} and wraps them
     * in a {@link CollectionModel} instance.
     *
     * @param entities must not be {@literal null}.
     * @return {@link CollectionModel} containing {@code D}.
     */
    @Override
    public CollectionModel<EntityModel<OrderDto>> toCollectionModel(Iterable<? extends OrderDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
