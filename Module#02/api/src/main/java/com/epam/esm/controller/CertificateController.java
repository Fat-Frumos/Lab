package com.epam.esm.controller;

import com.epam.esm.CertificateService;
import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.CertificateDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    @GetMapping(value = "/{id}")
    public final CertificateDto getOne(
            @PathVariable final Long id) {
        return certificateService.getById(id);
    }

    @GetMapping
    @ResponseBody
    public final List<CertificateDto> getAll() {
        return certificateService.getAll();
    }

    @PutMapping(value = "/{id}")
    public final Certificate update(
            @PathVariable("id") final Long id,
            @RequestBody final CertificateDto certificateDto) {
        certificateDto.setId(id);
        return certificateService.update(certificateDto);
    }

    @DeleteMapping(value = "/{id}")
    public final void delete(
            @PathVariable("id") final Long id) {
        certificateService.delete(id);
    }
}
