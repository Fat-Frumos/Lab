package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CertificateNotFoundException;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.dao.Queries.FETCH_GRAPH;
import static com.epam.esm.dao.Queries.SELECT_ALL_BY_IDS;
import static com.epam.esm.dao.Queries.SELECT_ALL_CERTIFICATES;
import static com.epam.esm.dao.Queries.SELECT_BY_NAME;
import static com.epam.esm.dao.Queries.SELECT_CERTIFICATES_BY_ORDER_ID;
import static com.epam.esm.dao.Queries.SELECT_CERTIFICATES_BY_USER_ID;
import static com.epam.esm.dao.Queries.SELECT_TAGS_BY_ID;
import static com.epam.esm.dao.Queries.SELECT_TAGS_BY_NAME;
import static com.epam.esm.dao.Queries.SELECT_TAG_BY_MANE;
import static com.epam.esm.dao.Queries.TAGS;
import static java.util.stream.Collectors.toSet;

/**
 * The implementation of the CertificateDao interface.
 * <p>
 * This class provides the concrete implementation
 * for accessing and manipulating certificates in the database.
 * <p>
 * It utilizes the EntityManagerFactory and EntityManager
 * to interact with the database.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CertificateDaoImpl implements CertificateDao {

    /**
     * The entity manager factory used for obtaining the entity manager.
     */
    @PersistenceUnit
    private final EntityManagerFactory factory;

    /**
     * Retrieves a certificate by its ID.
     *
     * @param id the ID of the certificate
     * @return an optional containing the certificate if found,
     * or an empty optional if not found
     */
    @Override
    public Optional<Certificate> getById(final Long id) {
        return Optional.ofNullable(findById(id));
    }

    /**
     * Retrieves a certificate by its name.
     *
     * @param name the name of the certificate
     * @return an optional containing the certificate if found,
     * or an empty optional if not found
     */
    @Override
    public Optional<Certificate> getByName(
            final String name) {
        try (EntityManager entityManager
                     = factory.createEntityManager()) {
            List<Certificate> certificates = entityManager
                    .createQuery(SELECT_BY_NAME, Certificate.class)
                    .setParameter("name", name)
                    .getResultList();
            return certificates.isEmpty()
                    ? Optional.empty()
                    : Optional.of(certificates.get(0));
        }
    }

    /**
     * Retrieves all certificates with pagination.
     *
     * @param pageable the pagination information
     * @return a list of certificates based
     * on the pagination parameters
     */
    @Override
    public List<Certificate> getAllBy(
            final Pageable pageable) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            EntityGraph<Certificate> graph = entityManager
                    .createEntityGraph(Certificate.class);
            graph.addAttributeNodes("tags");
            return entityManager.createQuery("SELECT c FROM Certificate c",
                            Certificate.class)
                    .setHint(FETCH_GRAPH, graph)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        }
    }

    /**
     * Retrieves all certificates with pagination using NativeQuery.
     *
     * @param pageable the pagination information
     * @return a list of certificates based
     * on the pagination parameters
     */
    @Override
    public List<Certificate> getAll(
            final Pageable pageable) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            return entityManager.createNativeQuery(SELECT_ALL_CERTIFICATES)
                    .setParameter("offset", pageable.getPageNumber()
                            * pageable.getPageSize())
                    .setParameter("limit", pageable.getPageSize())
                    .unwrap(NativeQuery.class)
                    .addEntity(Certificate.class)
                    .getResultList();
        }
    }

    /**
     * <p>Retrieves a certificate by its ID.</p>
     * <p>
     * This method fetches a certificate
     * from the database based on its ID.
     * <p>
     * It creates an entity graph to include the "tags"
     * attribute when retrieving the certificate.
     * <p>
     * The entity manager is used to find the certificate using
     * the specified ID and the entity graph hints.
     *
     * @param id the ID of the certificate
     * @return the certificate with the specified ID,
     * or null if not found
     */
    @Override
    public Certificate findById(final Long id) {
        try (EntityManager entityManager
                     = factory.createEntityManager()) {
            EntityGraph<Certificate> graph
                    = entityManager
                    .createEntityGraph(Certificate.class);
            graph.addAttributeNodes("tags");
            Map<String, Object> hints = new HashMap<>();
            hints.put(FETCH_GRAPH, graph);
            return entityManager.find(Certificate.class, id, hints);
        }
    }

    /**
     * <p>Saves a certificate</p>
     * <p>
     * This method saves the specified certificate to the database.
     * </p>
     * The entity manager is used to perform the transaction
     * and persist the certificate object.
     *
     * @param certificate the certificate to be saved
     * @return the saved certificate
     */
    @Override
    public Certificate save(final Certificate certificate) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            EntityTransaction transaction =
                    entityManager.getTransaction();
            try {
                transaction.begin();
                setCertificateTags(entityManager, certificate);
                entityManager.persist(certificate);
                transaction.commit();
                return certificate;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new CertificateNotFoundException(
                        e.getMessage());
            }
        }
    }

    /**
     * Removes the certificate with the specified ID from the database.
     * <p>
     * It uses the EntityManager and EntityTransaction
     * to perform the deletion in a transactional manner.
     * <p>
     * If any exception occurs during the process,
     * the transaction is rolled back
     * and a PersistenceException is thrown.
     *
     * @param id the ID of the certificate to be deleted
     * @throws PersistenceException if an error
     *                              occurs during the deletion process
     */
    @Override
    public void delete(final Long id) {
        try (EntityManager entityManager
                     = factory.createEntityManager()) {
            EntityTransaction transaction
                    = entityManager.getTransaction();
            try {
                transaction.begin();
                Certificate entity = entityManager
                        .getReference(Certificate.class, id);
                entityManager.remove(entity);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new PersistenceException(
                        e.getMessage(), e);
            }
        }
    }

    /**
     * Updates a certificate in the database.
     * <p>
     * The method updates a certificate in the database.
     * It mentions the usage of EntityManager and EntityTransaction
     * for performing the update in a transactional manner.
     * <p>
     * The method checks if the certificate exists by its ID and throws
     * a CertificateNotFoundException if it doesn't.
     * It then updates the fields of the existing certificate based
     * on the provided certificate object.
     *
     * @param certificate the certificate object with updated values
     * @return the updated certificate
     * @throws CertificateNotFoundException if the certificate
     *                                      does not exist in the database
     * @throws PersistenceException         if an error occurs
     *                                      during the update process
     */
    @Override
    public Certificate update(Certificate certificate) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                Certificate existed = entityManager.getReference(Certificate.class, certificate.getId());

                if (existed == null) {
                    throw new CertificateNotFoundException("Certificate not found with id " + certificate.getId());
                }
                if (certificate.getName() != null) {
                    existed.setName(certificate.getName());
                }
                if (certificate.getDescription() != null) {
                    existed.setDescription(certificate.getDescription());
                }
                if (certificate.getPrice() != null) {
                    existed.setPrice(certificate.getPrice());
                }
                if (certificate.getDuration() != null) {
                    existed.setDuration(certificate.getDuration());
                }
                if (certificate.getTags() != null) {
                    setCertificateTags(entityManager, certificate, existed);
                }
                log.info(existed.toString());
                transaction.commit();
                return existed;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new PersistenceException(e.getMessage());
            }
        }
    }

    /**
     * Retrieves the tags associated with a certificate by its ID.
     * <p>
     * This method retrieves the tags associated with
     * a certificate from the database based on its ID.
     *
     * @param id the ID of the certificate
     * @return a list of tags associated with the certificate
     */
    @Override
    public List<Tag> findTagsByCertificateId(final Long id) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            return entityManager
                    .createQuery(SELECT_TAGS_BY_ID, Tag.class)
                    .setParameter("id", id)
                    .getResultList();
        }
    }

    /**
     * Retrieves certificates based on a list of tag names.
     * <p>
     * This method retrieves certificates from the database
     * that are associated with any of the specified tag names.
     * <p>
     * It uses the EntityManager to create a query that selects
     * the certificates based on the provided tag names.
     * <p>
     * The method sets the tag names as parameters in the query
     * and returns the resulting list of certificates.
     *
     * @param tagNames the list of tag names
     * @return a list of certificates associated
     * with any of the specified tag names
     */
    @Override
    public List<Certificate> findByTagNames(final List<String> tagNames) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Certificate> query = builder.createQuery(Certificate.class);
            Root<Certificate> root = query.from(Certificate.class);
            query.select(root).where(builder.and(tagNames.stream()
                    .map(tagName -> builder.equal(root
                            .join("tags", JoinType.INNER)
                            .get("name"), tagName))
                    .toArray(Predicate[]::new)));

            EntityGraph<Certificate> graph = entityManager
                    .createEntityGraph(Certificate.class);
            graph.addAttributeNodes("tags");

            return entityManager
                    .createQuery(query)
                    .setHint(FETCH_GRAPH, graph)
                    .getResultList();
        }
    }


    /**
     * Retrieves the certificates associated with a user by their ID.
     * <p>
     * This method retrieves the certificates associated with
     * a user from the database based on their ID.
     *
     * @param id the ID of the user
     * @return a list of certificates associated with the user
     */
    @Override
    public List<Certificate> getCertificatesByUserId(Long id) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityGraph<Certificate> graph = entityManager
                    .createEntityGraph(Certificate.class);
            graph.addAttributeNodes(TAGS);
            return entityManager.createQuery(
                            SELECT_CERTIFICATES_BY_USER_ID, Certificate.class)
                    .setParameter("id", id)
                    .setHint(FETCH_GRAPH, graph)
                    .getResultList();
        }
    }

    /**
     * Retrieves the certificates based on a set of certificate IDs.
     * <p>
     * This method retrieves the certificates from
     * the database that match the IDs specified in the set.
     * <p>
     * The method sets the IDs parameter
     * in the query and returns the resulting set of certificates.
     *
     * @param certificateIds the set of certificate IDs
     * @return a set of certificates matching the specified IDs
     */
    public Set<Certificate> findAllByIds(Set<Long> certificateIds) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityGraph<Certificate> graph = entityManager
                    .createEntityGraph(Certificate.class);
            graph.addAttributeNodes(TAGS);
            List<Certificate> certificates = entityManager
                    .createQuery(SELECT_ALL_BY_IDS, Certificate.class)
                    .setParameter("ids", certificateIds)
                    .setHint(FETCH_GRAPH, graph)
                    .getResultList();
            return new HashSet<>(certificates);
        }
    }

    /**
     * Retrieves the certificates associated with an order by its ID.
     * <p>
     * This method retrieves the certificates associated with
     * an order from the database based on its ID.
     * <p>
     * The method sets the order ID parameter in the query
     * and returns the resulting set of certificates.
     *
     * @param orderId the ID of the order
     * @return a set of certificates associated with the order
     */
    public Set<Certificate> findAllByOrderId(final Long orderId) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityGraph<Certificate> graph = entityManager
                    .createEntityGraph(Certificate.class);
            graph.addAttributeNodes("tags");
            graph.addSubgraph("orders")
                    .addAttributeNodes("user");

            List<Certificate> list = entityManager.createQuery(
                            SELECT_CERTIFICATES_BY_ORDER_ID, Certificate.class)
                    .setParameter("orderId", orderId)
                    .setHint(FETCH_GRAPH, graph).getResultList();
            return new HashSet<>(list);
        }
    }

    /**
     * Sets the tags for a certificate using the provided EntityManager.
     * <p>
     * It maps the existing tags of the certificate
     * to the corresponding tags fetched from the database based on their names.
     * The method updates the certificate's tags with the fetched tags.
     *
     * @param entityManager the EntityManager used for database access
     * @param certificate   the certificate for which to set the tags
     */
    private void setCertificateTags(
            final EntityManager entityManager,
            final Certificate certificate) {
        Set<Tag> tagSet = certificate.getTags().stream()
                .map(tag -> entityManager.createQuery(
                                SELECT_TAGS_BY_NAME, Tag.class)
                        .setParameter("name", tag.getName())
                        .getResultList().stream()
                        .findFirst()
                        .orElse(tag))
                .collect(toSet());
        certificate.setTags(tagSet);
    }

    private void setCertificateTags(
            EntityManager entityManager,
            Certificate certificate,
            Certificate existed) {

        Set<Tag> updatedTags = new HashSet<>();
        for (Tag tag : certificate.getTags()) {

            Tag existingTag = entityManager
                    .createQuery(SELECT_TAG_BY_MANE, Tag.class)
                    .setParameter("name", tag.getName())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            updatedTags.add(existingTag != null ? existingTag : tag);
        }
        existed.setTags(updatedTags);
    }
}
