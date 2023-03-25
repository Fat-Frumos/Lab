package com.epam.esm.controller;

import com.epam.esm.CertificateService;
import com.epam.esm.controller.exception.ErrorResponse;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.ServiceException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final CertificateService certificateService;
    private static final Logger LOGGER = LogManager.getLogger();

    @ResponseBody
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getCertificateById(
            @PathVariable("id") final Long id) {
        try {
            return new ResponseEntity<>(
                    certificateService.getById(id),
                    new HttpHeaders(),
                    HttpStatus.OK);
        } catch (ServiceException e) {
            LOGGER.debug(e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse(
                            e.getMessage(), 40401),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) { //TODO enums status
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(
                    new ErrorResponse(e.getMessage(), 500),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Object> getAll() {
        try {
            return new ResponseEntity<>(
                    certificateService.getAll(),
                    new HttpHeaders(),
                    HttpStatus.OK);
        } catch (ServiceException e) {
            LOGGER.debug(e.getMessage());
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND);
        }
    }

    @ResponseBody
    @PutMapping(value = "/{id}")
    public final ResponseEntity<Object> update(
            @PathVariable("id") final Long id,
            @RequestBody final CertificateDto certificateDto) {
        certificateDto.setId(id);
        try {
            return new ResponseEntity<>(
                    certificateService.update(certificateDto),
                    new HttpHeaders(),
                    HttpStatus.OK);
        } catch (ServiceException e) {
            LOGGER.debug(e.getMessage());
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND);
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/{id}")
    public final ResponseEntity<Void> delete(
            @PathVariable("id") final Long id) {
        return certificateService.delete(id)
                ? new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
