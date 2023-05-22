package com.epam.esm.controller.assembler;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class OrderAssembler
        implements RepresentationModelAssembler<OrderDto, EntityModel<OrderDto>> {

    /**
     * Converts the given entity into a {@code D},
     * which extends {@link RepresentationModelAssembler}.
     *
     * @param orderDto must not be {@literal null}.
     * @return EntityModel<OrderDto>
     */
    @NonNull
    @Override
    public EntityModel<OrderDto> toModel(@NonNull final OrderDto orderDto) {
        return EntityModel.of(orderDto,
                linkTo(methodOn(OrderController.class).getOrderById(orderDto.getId())).withSelfRel(),
                linkTo(methodOn(CertificateController.class)
                        .getCertificatesByIds(orderDto
                                .getCertificateDtos()
                                .stream()
                                .map(CertificateDto::getId)
                                .collect(toSet()))).withRel("certificates"));
    }

    /**
     * Converts an {@link Iterable} or {@code T}s into
     * an {@link Iterable} of {@link RepresentationModelAssembler}
     * and wraps them in a {@link CollectionModel} instance.
     *
     * @param entities must not be {@literal null}.
     * @return {@link CollectionModel} containing {@code D}.
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
                        .getAllOrders(PageRequest.of(0, 25, Sort.by("name").ascending())))
                        .withSelfRel());
    }
}
