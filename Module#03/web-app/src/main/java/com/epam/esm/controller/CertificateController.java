package com.epam.esm.controller;

import com.epam.esm.assembler.CertificateAssembler;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.FilterParams;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.Linkable;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.SortOrder;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/certificates")
public class CertificateController {

    private final CertificateAssembler assembler;
    private final CertificateService certificateService;

    @GetMapping(value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public EntityModel<Linkable> getCertificateById(
            @PathVariable final Long id) {
        return assembler.toModel(
                certificateService.getById(id));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public CollectionModel<EntityModel<Linkable>> getAll(
            @RequestParam(defaultValue = "UNSORTED") SortOrder sort,
            @RequestParam(defaultValue = "ID") FilterParams params,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return assembler.toCollectionModel(
                certificateService.getAllWithoutTags(
                        Criteria.builder()
                                .filterParams(params)
                                .sortOrder(sort)
                                .page(page)
                                .size(size)
                                .build()));
    }

    @GetMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public CollectionModel<EntityModel<Linkable>> search(
            @RequestParam(required = false) List<String> tagNames) {
        return assembler.toCollectionModel(
                certificateService.findCertificatesByTags(tagNames));
    }

    @PatchMapping(value = "/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public EntityModel<Linkable> update(
            @RequestBody final CertificateDto dto,
            @PathVariable final Long id) {
        return assembler.toModel(
                certificateService.update(dto, id));
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public EntityModel<Linkable> create(
            @RequestBody final CertificateDto dto) {
        return assembler.toModel(
                certificateService.save(dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable final Long id) {
        certificateService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @GetMapping(value = "/{id}/tags",
            produces = APPLICATION_JSON_VALUE)
    public List<TagDto> getTagsByCertificateId(
            @PathVariable final Long id) {
        return certificateService.findTagsByCertificateId(id);
    }
}
