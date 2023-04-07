package com.epam.esm.controller;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.SortOrder;
import com.epam.esm.exception.CertificateIsExistsException;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.handler.ErrorResponse;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.service.CertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCertificateById(
            @PathVariable("id") final Long id) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(
                    certificateService.getById(id), headers, OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse(
                            e.getMessage(), 40401), NOT_FOUND);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAll() {
        try {
            return new ResponseEntity<>(
                    certificateService.getAllWithoutTags(),
                    new HttpHeaders(), OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> search(
            @RequestParam(value = "name", required = false) final String name,
            @RequestParam(value = "date", required = false) final Instant date,
            @RequestParam(value = "tagName", required = false) final String tagName,
            @RequestParam(value = "description", required = false) final String description,
            @RequestParam(value = "order", required = false, defaultValue = "UNSORTED") final SortOrder order) {
        try {
            Criteria criteria = Criteria.builder()
                    .tagName(tagName)
                    .name(name).date(date)
                    .description(description)
                    .sortOrder(order).build();
            return new ResponseEntity<>(
                    certificateService.getAllBy(criteria),
                    new HttpHeaders(), OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @ResponseBody
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<Object> update(
            @PathVariable("id") final Long id,
            @RequestBody final CertificateDto certificateDto) {
        try {
            if (certificateService.getById(id) != null) {
                certificateDto.setId(id);
                return new ResponseEntity<>(
                        certificateService.update(certificateDto),
                        new HttpHeaders(), OK);
            }
            throw new NotFoundException("update", "/certificates/" + id, new HttpHeaders());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @ResponseBody
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public final ResponseEntity<Object> create(
            @RequestBody final CertificateDto certificateDto) {
        try {
            if (certificateService.getByName(certificateDto.getName()) == null) {
                return new ResponseEntity<>(
                        certificateService.save(certificateDto),
                        new HttpHeaders(), OK);
            } else {
                throw new CertificateIsExistsException(certificateDto.getName());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/{id}")
    public final ResponseEntity<Void> delete(
            @PathVariable("id") final Long id) {
        try {
            return certificateService.delete(id)
                    ? new ResponseEntity<>(OK)
                    : new ResponseEntity<>(NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }
}
