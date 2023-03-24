package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.CertificateDto;

public interface CertificateDao extends Dao<Certificate> {
    CertificateDto update(Certificate certificate);
}
