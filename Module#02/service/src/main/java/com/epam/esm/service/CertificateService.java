package com.epam.esm.service;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * This interface defines the operations
 * that can be performed on a certificate entity.
 * <p>
 * The interface is validated using the @Validated annotation,
 * which ensures that the input parameters
 * <p>
 * are valid before any operation is performed.
 */
@Validated
public interface CertificateService {
    /**
     * Retrieves the certificate with the specified ID.
     *
     * @param id the ID of the certificate to retrieve
     * @return the CertificateDto representing
     * the certificate with the specified ID
     */
    CertificateDto getById(Long id);

    /**
     * Retrieves all the certificates.
     *
     * @return a list of CertificateDto
     * representing all the certificates
     */
    List<CertificateDto> getAll();

    /**
     * Retrieves the certificate with the specified name.
     *
     * @param name the name of the certificate to retrieve
     * @return the CertificateDto representing
     * the certificate with the specified name
     */
    CertificateDto getByName(String name);

    /**
     * Deletes the certificate with the specified ID.
     *
     * @param id the ID of the certificate to delete
     */
    void delete(Long id);

    /**
     * Retrieves all the certificates that match the specified criteria.
     *
     * @param criteria the search criteria to use
     * @return a list of CertificateDto representing
     * the certificates that match the specified criteria
     */
    List<CertificateDto> getAllBy(Criteria criteria);

    /**
     * Updates the certificate with the specified ID.
     *
     * @param dto the CertificateDto representing the updated certificate
     * @return the CertificateDto representing the updated certificate
     */
    CertificateDto update(CertificateDto dto);

    /**
     * Retrieves all the certificates without their tags.
     *
     * @return a list of CertificateWithoutTagDto
     * representing all the certificates without their tags
     */

    List<CertificateWithoutTagDto> getAllWithoutTags();

    /**
     * Saves the specified certificate.
     *
     * @param dto the CertificateDto representing the certificate to save
     * @return the CertificateDto representing the saved certificate
     */
    CertificateDto save(CertificateDto dto);
}
