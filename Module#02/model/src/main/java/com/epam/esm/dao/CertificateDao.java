package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;
import com.epam.esm.criteria.Criteria;

import java.util.List;

public interface CertificateDao extends Dao<Certificate> {

    boolean update(Certificate certificate);

    List<Certificate> getAllBy(Criteria criteria);
}
