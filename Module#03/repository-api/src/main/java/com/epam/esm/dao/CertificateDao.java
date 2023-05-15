package com.epam.esm.dao;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;

import java.util.List;

public interface CertificateDao extends Dao<Certificate> {

    List<Certificate> getAll(Criteria criteria);

    Certificate findById(Long id);

    Certificate update(Certificate certificate, Long id);

    List<TagDto> findTagsByCertificateId(Long id);

    List<Certificate> findByTagNames(List<String> tagNames);

    List<Certificate> getCertificatesByUserId(Long userId);

    List<Certificate> findAllByIds(List<Long> certificateIds);
}
