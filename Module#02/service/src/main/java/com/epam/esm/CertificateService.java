package com.epam.esm;

import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.CertificateDto;

import java.util.List;

public interface CertificateService {

    CertificateDto getById(Long id);

    List<CertificateDto> getAll();

    Certificate update(CertificateDto certificateDto);
    boolean delete(Long id);
}
