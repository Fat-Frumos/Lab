package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;

public interface CertificateDao extends Dao<Certificate> {
    boolean update(Certificate certificate);
}
