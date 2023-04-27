package com.epam.esm.controller;

import com.epam.esm.assembler.CertificateAssembler;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.Dto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/certificates")
public class CertificateController {

    private final CertificateAssembler assembler;
    private final CertificateService certificateService;

    @GetMapping(value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public EntityModel<Dto> getCertificateById(
            @PathVariable final Long id) {
        return assembler.toModel(
                certificateService.getById(id));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public CollectionModel<EntityModel<Dto>> getAll() {
        return assembler.toCollectionModel(
                certificateService.getAllWithoutTags());
    }

    @GetMapping(value = "/",
            produces = APPLICATION_JSON_VALUE)
    public List<CertificateDto> search(
            @RequestBody final Criteria criteria) {
        return criteria != null
                ? certificateService.getAllBy(criteria)
                : certificateService.getAllBy(
                        Criteria.builder().offset(0L).size(25L).build());
    }

    @PatchMapping(value = "/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public EntityModel<Dto> update(
            @RequestBody final CertificateDto dto,
            @PathVariable final Long id) {
        return assembler.toModel(
                certificateService.update(dto, id));
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public CertificateDto create(
            @RequestBody final CertificateDto dto) {
        return certificateService.save(dto);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void delete(
            @PathVariable final Long id) {
        certificateService.delete(id);
    }

    @GetMapping(value = "/{id}/tags",
            produces = APPLICATION_JSON_VALUE)
    public List<TagDto> getTagsByCertificateId(
            @PathVariable final Long id) {
        return certificateService.findTagsByCertificateId(id);
    }
}
