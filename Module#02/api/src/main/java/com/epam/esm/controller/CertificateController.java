package com.epam.esm.controller;

import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.handler.ErrorResponse;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.service.CertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
        } catch (RuntimeException e) {
            log.info(e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse(
                            e.getMessage(), 40401), NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(
                    new ErrorResponse(
                            e.getMessage(), 404), NOT_FOUND);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAll() {
        try {
            return new ResponseEntity<>(
                    certificateService.getAll(),
                    new HttpHeaders(), OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @ResponseBody
    @PatchMapping(value = "/{id}")
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
            throw new CertificateNotFoundException(String.valueOf(id));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/{id}")
    public final ResponseEntity<Void> delete(
            @PathVariable("id") final Long id) {
        return certificateService.delete(id)
                ? new ResponseEntity<>(OK)
                : new ResponseEntity<>(NOT_FOUND);
    }
}
