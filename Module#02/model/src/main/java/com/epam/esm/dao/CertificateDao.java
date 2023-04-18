package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.criteria.Criteria;

import java.util.List;

/**
 * This interface extends the Dao interface
 * and specifies additional methods
 * for managing Certificate entities in the database.
 * <p>
 * It includes a method for updating an existing Certificate
 * and retrieving a list of Certificates based on a set of criteria.
 */
public interface CertificateDao extends Dao<Certificate> {

    /**
     * Updates an existing Certificate in the database.
     *
     * @param certificate the Certificate to be updated
     * @return true if the update was successful, false otherwise
     */
    boolean update(Certificate certificate);

    /**
     * Retrieves a list of Certificates from the database based on a set of criteria.
     *
     * @param criteria the criteria to be used for filtering the Certificates
     * @return a List of Certificates that match the criteria
     */
    List<Certificate> getAllBy(Criteria criteria);
}
