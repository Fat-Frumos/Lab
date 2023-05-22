package com.epam.esm.controller;

import com.epam.esm.controller.assembler.CertificateAssembler;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.CertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/certificates")
public class CertificateController {

    private final CertificateAssembler assembler;
    private final CertificateService certificateService;

    @GetMapping(value = "/{id}")
    public EntityModel<CertificateDto> getCertificateById(
            @PathVariable final Long id) {
        return assembler.toModel(
                certificateService.getById(id));
    }

    @GetMapping
    public CollectionModel<EntityModel<CertificateDto>> getAll(
            @PageableDefault(size = 25, sort = {"id"},
                    direction = Sort.Direction.ASC)
            final Pageable pageable) {
        return assembler.toCollectionModel(
                certificateService.getAllWithoutTags(pageable));
    }

    @GetMapping(value = "/")
    public CollectionModel<EntityModel<CertificateDto>> search(
            @RequestParam(required = false) List<String> tagNames) {
        return assembler.toCollectionModel(
                certificateService.findCertificatesByTags(tagNames));
    }

    @PatchMapping(value = "/", consumes = APPLICATION_JSON_VALUE)
    public EntityModel<CertificateDto> update(
            @RequestBody final CertificateDto dto) {
        return assembler.toModel(
                certificateService.update(dto));
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public EntityModel<CertificateDto> create(
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

    @GetMapping(value = "/{id}/tags")
    public List<TagDto> getTagsByCertificateId(
            @PathVariable final Long id) {
        return certificateService.findTagsByCertificateId(id);
    }

    @GetMapping(value = "/orders/{ids}")
    public CollectionModel<EntityModel<CertificateDto>>
    getCertificatesByIds(
            @PathVariable final Set<Long> ids) {
        return assembler.toCollectionModel(
                certificateService.getByIds(ids));
    }

    @GetMapping(value = "/users/{id}")
    public CollectionModel<EntityModel<CertificateDto>> getUserCertificates(
            @PathVariable final Long id) {
        return assembler.toCollectionModel(
                certificateService.getCertificatesByUserId(id));
    }
}
