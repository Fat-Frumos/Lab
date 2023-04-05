package com.epam.esm.service;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;

import java.util.List;

public interface CertificateService extends BaseService<CertificateDto> {

    List<CertificateDto> getAllBy(Criteria criteria);

    boolean update(CertificateDto certificateDto);

    List<CertificateWithoutTagDto> getAllWithoutTags();
}
