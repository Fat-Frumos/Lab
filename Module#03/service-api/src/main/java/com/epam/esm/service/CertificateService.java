package com.epam.esm.service;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.dto.TagDto;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface CertificateService {
    CertificateDto getById(Long id);

    List<CertificateDto> getAll();

    CertificateDto getByName(String name);

    void delete(Long id);

    CertificateDto update(CertificateDto dto, Long id);

    List<CertificateWithoutTagDto> getAllWithoutTags(Criteria criteria);

    CertificateDto save(CertificateDto dto);

    List<TagDto> findTagsByCertificateId(Long id);

    List<CertificateDto> findCertificatesByTags(List<String> tagNames);

    List<CertificateDto> getCertificatesByUserId(Long userId);
}
