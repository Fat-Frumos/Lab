package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * This interface provides methods for accessing
 * and manipulating Certificates in the data source.
 */
public interface CertificateDao extends Dao<Certificate> {

    /**
     * Retrieves all certificates with pagination.
     *
     * @param pageable the pagination information
     * @return the list of certificates
     */
    List<Certificate> getAllBy(Pageable pageable);

    /**
     * Finds a certificate by its ID.
     *
     * @param id the ID of the certificate to find
     * @return the found certificate, or null if not found
     */
    Certificate findById(Long id);

    /**
     * Updates the specified certificate.
     *
     * @param certificate the certificate to update
     * @return the updated certificate
     */
    Certificate update(Certificate certificate);

    /**
     * Finds the tags associated with a certificate specified by ID.
     *
     * @param id the ID of the certificate
     * @return the list of tags associated with the certificate
     */
    List<Tag> findTagsByCertificateId(Long id);

    /**
     * Finds certificates that have any of the specified tag names.
     *
     * @param tagNames the list of tag names to search for
     * @return the list of certificates matching the tag names
     */
    List<Certificate> findByTagNames(List<String> tagNames);

    /**
     * Retrieves all certificates associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return the list of certificates associated with the user
     */
    List<Certificate> getCertificatesByUserId(Long userId);

    /**
     * Retrieves a set of certificates by their IDs.
     *
     * @param certificateIds the set of certificate IDs
     * @return the set of certificates matching the IDs
     */
    Set<Certificate> findAllByIds(Set<Long> certificateIds);

    /**
     * Retrieves all certificates associated with a specific order ID.
     *
     * @param id the ID of the order
     * @return the set of certificates associated with the order
     */
    Set<Certificate> findAllByOrderId(Long id);

    /**
     * Retrieves all slim certificates with pagination.
     *
     * @param pageable the pagination information
     * @return the list of certificates
     */
    List<Certificate> getAll(Pageable pageable);
}
