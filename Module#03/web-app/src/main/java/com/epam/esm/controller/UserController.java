package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.FilterParams;
import com.epam.esm.dto.Linkable;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.SortOrder;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserAssembler assembler;

    private final UserService userService;
    private final CertificateService certificateService;

    @GetMapping("/{id}")
    public EntityModel<Linkable> getUser(
            @PathVariable final Long id) {
        return assembler.toModel(
                userService.getById(id));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public CollectionModel<EntityModel<Linkable>> getUsers(
            @RequestParam(defaultValue = "UNSORTED") SortOrder sort,
            @RequestParam(defaultValue = "ID") FilterParams params,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return assembler.toCollectionModel(
                userService.getAll(Criteria
                        .builder()
                        .filterParams(params)
                        .sortOrder(sort)
                        .page(page)
                        .size(size)
                        .build()));
    }

    @GetMapping(value = "/{id}/certificates", produces = APPLICATION_JSON_VALUE)
    public CollectionModel<EntityModel<Linkable>> getUserCertificates(
            @PathVariable final Long id) {
        return assembler.toCollectionModel(
                certificateService.getCertificatesByUserId(id));
    }
}
