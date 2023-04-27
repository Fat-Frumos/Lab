package com.epam.esm.dao;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.criteria.Criteria;

import java.util.List;

public interface CertificateDao extends Dao<Certificate> {

    List<Certificate> getAllBy(Criteria criteria);

    Certificate findById(Long id);

    Certificate update(Certificate certificate, Long id);

    List<TagDto> findTagsByCertificateId(Long id);
}
