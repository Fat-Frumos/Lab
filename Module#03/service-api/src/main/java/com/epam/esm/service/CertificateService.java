package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface CertificateService {
    CertificateDto getById(Long id);

    Page<CertificateDto> getAll(Pageable pageable);

    CertificateDto getByName(String name);

    void delete(Long id);

    CertificateDto update(CertificateDto dto);

    Page<CertificateDto> getAllWithoutTags(Pageable pageable);

    CertificateDto save(CertificateDto dto);

    List<TagDto> findTagsByCertificateId(Long id);

    Page<CertificateDto> findCertificatesByTags(List<String> tagNames);

    Page<CertificateDto> getCertificatesByUserId(Long userId);

    Page<CertificateDto> getByIds(Set<Long> ids);
}
