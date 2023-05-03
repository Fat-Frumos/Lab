package com.epam.esm.assembler;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.dto.Linkable;
import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateAssembler
        implements RepresentationModelAssembler<Linkable, EntityModel<Linkable>> {

    @NonNull
    @Override
    public EntityModel<Linkable> toModel(@NonNull final Linkable dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(CertificateController.class).getTagsByCertificateId(dto.getId())).withRel("tags"),
                linkTo(methodOn(CertificateController.class).getCertificateById(dto.getId())).withSelfRel(),
                linkTo(methodOn(CertificateController.class).delete(dto.getId())).withRel("delete")
//                linkTo(methodOn(CertificateController.class).create(dto)).withRel("create"),
//                linkTo(methodOn(CertificateController.class).update(dto, dto.getId())).withRel("update"),
//                linkTo(methodOn(OrderController.class).getAllOrdersByCertificateId(dto.getId())).withRel("orders")
        );
    }

    @NonNull
    @Override
    public CollectionModel<EntityModel<Linkable>> toCollectionModel(
            final Iterable<? extends Linkable> entities) {
        return CollectionModel.of(StreamSupport
                        .stream(entities.spliterator(), false)
                        .map(this::toModel)
                        .collect(toList()),
                linkTo(methodOn(CertificateController.class)
                        .getAll())
                        .withSelfRel());
//                        .search(Criteria.builder().page(25L).offset(0L).sortBy("id").build()))
    }
}
