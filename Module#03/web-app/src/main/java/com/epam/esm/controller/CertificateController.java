package com.epam.esm.controller;

import com.epam.esm.assembler.CertificateAssembler;
import com.epam.esm.assembler.TagAssembler;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.CertificateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * Controller class for handling certificate-related operations.
 * {@link RestController} to indicate that it is a Spring MVC controller,
 * {@link RequestMapping} with a value of "/certificates" to map requests,
 * and {@link CrossOrigin} with origins set to "*"
 * and allowed headers set to "GET", "POST", "PUT", and "DELETE"
 * to enable Cross-Origin Resource Sharing (CORS).
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/certificates")
@CrossOrigin(origins = "*", allowedHeaders = {"GET", "POST", "PUT", "DELETE"})
public class CertificateController {
    /**
     * The Certificate assembler for converting Certificate entities to DTOs.
     */
    private final CertificateAssembler assembler;
    /**
     * The tag assembler for converting tag entities to DTOs.
     */
    private final TagAssembler tagAssembler;
    /**
     * The Certificate service for performing tag-related operations.
     */
    private final CertificateService certificateService;

    /**
     * Retrieves a certificate by its ID.
     *
     * @param id the ID of the certificate
     * @return the EntityModel representation of the certificate
     */
    @GetMapping("/{id}")
    public EntityModel<CertificateDto> getCertificateById(
            @Valid @PathVariable final Long id) {
        return assembler.toModel(
                certificateService.getById(id));
    }

    /**
     * Retrieves all certificates.
     *
     * @param pageable the pageable information for pagination and sorting
     * @return the CollectionModel representation of all certificates
     */
    @GetMapping("/")
    public CollectionModel<EntityModel<CertificateDto>> getAllBy(
            @PageableDefault(size = 25, sort = {"id"},
                    direction = Sort.Direction.ASC)
            final Pageable pageable) {
        return assembler.toCollectionModel(
                certificateService.getAll(pageable));
    }

    /**
     * Retrieves all certificates.
     *
     * @param pageable the pageable information for pagination and sorting
     * @return the CollectionModel representation of all slim certificates
     */
    @GetMapping()
    public CollectionModel<EntityModel<CertificateDto>> getAll(
            @PageableDefault(size = 25, sort = {"id"},
                    direction = Sort.Direction.ASC)
            final Pageable pageable) {
        return assembler.toCollectionModel(
                certificateService.getSlimCertificates(pageable));
    }

    /**
     * Searches for certificates by tag names.
     *
     * @param tagNames the list of tag names to search for
     * @return the CollectionModel representation of the search results
     */
    @GetMapping(value = "/search")
    public CollectionModel<EntityModel<CertificateDto>> search(
            @RequestParam(required = false) final List<String> tagNames) {
        return assembler.toCollectionModel(
                certificateService.findAllByTags(tagNames));
    }

    /**
     * Updates a certificate.
     *
     * @param id  the ID of the certificate to update
     * @param dto the updated certificate data
     * @return the EntityModel representation of the updated certificate
     */
    @PatchMapping(value = "/{id}")
    public EntityModel<CertificateDto> update(
            @Valid @PathVariable final Long id,
            @Valid @RequestBody final CertificateDto dto) {
        dto.setId(id);
        return assembler.toModel(
                certificateService.update(dto));
    }

    /**
     * Creates a new certificate.
     *
     * @param dto the data of the certificate to create
     * @return the created certificate
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public CertificateDto create(
            @Valid @RequestBody final CertificateDto dto) {
        return certificateService.save(dto);
    }

    /**
     * Deletes a certificate by its ID.
     *
     * @param id the ID of the certificate to delete
     * @return the HTTP status response
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable final Long id) {
        certificateService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    /**
     * Retrieves the tags associated with a certificate.
     *
     * @param id the ID of the certificate
     * @return the CollectionModel representation of the tags
     */
    @GetMapping(value = "/{id}/tags")
    public CollectionModel<EntityModel<TagDto>> getTagsByCertificateId(
            @PathVariable final Long id) {
        return tagAssembler.toCollectionModel(
                certificateService.findTagsByCertificateId(id));
    }

    /**
     * Retrieves the certificates associated with a user.
     *
     * @param id the ID of the user
     * @return the CollectionModel representation of the certificates
     */
    @GetMapping(value = "/users/{id}")
    public CollectionModel<EntityModel<CertificateDto>> getUserCertificates(
            @PathVariable final Long id) {
        return assembler.toCollectionModel(
                certificateService.getCertificatesByUserId(id));
    }

    /**
     * Retrieves all certificates associated with an order.
     *
     * @param id the ID of the order
     * @return the CollectionModel representation of the certificates
     */
    @GetMapping(value = "/orders/{id}")
    public CollectionModel<EntityModel<CertificateDto>> getAllByOrderId(
            @PathVariable final Long id) {
        return assembler.toCollectionModel(
                certificateService.getByOrderId(id));
    }
}
