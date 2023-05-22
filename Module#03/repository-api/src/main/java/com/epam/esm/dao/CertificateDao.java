package com.epam.esm.dao;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface CertificateDao extends Dao<Certificate> {

    List<Certificate> getAll(Pageable pageable);

    Certificate findById(Long id);

    Certificate update(Certificate certificate);

    List<TagDto> findTagsByCertificateId(Long id);

    List<Certificate> findByTagNames(List<String> tagNames);

    List<Certificate> getCertificatesByUserId(Long userId);

    Set<Certificate> findAllByIds(Set<Long> certificateIds);
}
