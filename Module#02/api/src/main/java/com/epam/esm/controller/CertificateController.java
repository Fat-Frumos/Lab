package com.epam.esm.controller;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.SortOrder;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.service.CertificateService;
import lombok.RequiredArgsConstructor;
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

import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public CertificateDto getCertificateById(@PathVariable final Long id) {
        return certificateService.getById(id);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<CertificateWithoutTagDto> getAll() {
        return certificateService.getAllWithoutTags();
    }

    @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public List<CertificateDto> search(
            @RequestParam(required = false) final String name,
            @RequestParam(required = false) final Instant date,
            @RequestParam(required = false) final String tagName,
            @RequestParam(required = false) final String description,
            @RequestParam(required = false, defaultValue = "UNSORTED") final SortOrder order) {
        Criteria criteria = Criteria
                .builder()
                .tagName(tagName)
                .name(name).date(date)
                .description(description)
                .sortOrder(order)
                .build();
        return certificateService.getAllBy(criteria);
    }

    @PatchMapping(consumes = APPLICATION_JSON_VALUE)
    public final CertificateDto update(
            @RequestBody final CertificateDto dto) {
        return certificateService.update(dto);
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public final CertificateDto create(
            @RequestBody final CertificateDto dto) {
        return certificateService.save(dto);
    }

    @ResponseStatus(OK)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable final Long id) {
        certificateService.delete(id);
    }
}
