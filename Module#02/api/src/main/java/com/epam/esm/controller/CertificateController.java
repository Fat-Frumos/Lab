package com.epam.esm.controller;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.SortOrder;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.service.CertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CertificateDto getCertificateById(@PathVariable final Long id) {
        return certificateService.getById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CertificateWithoutTagDto> getAll() {
        return certificateService.getAllWithoutTags();
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CertificateDto> search(
            @RequestParam(value = "name", required = false) final String name,
            @RequestParam(value = "date", required = false) final Instant date,
            @RequestParam(value = "tagName", required = false) final String tagName,
            @RequestParam(value = "description", required = false) final String description,
            @RequestParam(value = "order", required = false, defaultValue = "UNSORTED") final SortOrder order) {

        Criteria criteria = Criteria.builder()
                .tagName(tagName)
                .name(name).date(date)
                .description(description)
                .sortOrder(order)
                .build();
        return certificateService.getAllBy(criteria);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public final Boolean update(@PathVariable final Long id,
            @RequestBody final CertificateDto certificateDto) {
        certificateDto.setId(id);
        return certificateService.update(certificateDto);
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public final Boolean create(
            @RequestBody final CertificateDto certificateDto) {
        return certificateService.save(certificateDto);
    }

    @DeleteMapping(value = "/{id}")
    public final Boolean delete(@PathVariable final Long id) {
        return certificateService.delete(id);
    }
}
