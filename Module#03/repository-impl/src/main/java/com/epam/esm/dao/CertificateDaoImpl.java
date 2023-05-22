package com.epam.esm.dao;

import com.epam.esm.dto.TagDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.dao.Queries.SELECT_ALL_BY_IDS;
import static com.epam.esm.dao.Queries.SELECT_BY_NAME;
import static com.epam.esm.dao.Queries.SELECT_CERTIFICATES_BY_USER_ID;
import static com.epam.esm.dao.Queries.SELECT_TAGS_BY_ID;
import static com.epam.esm.dao.Queries.SELECT_TAGS_BY_NAME;
import static java.util.stream.Collectors.toSet;

@Repository
@RequiredArgsConstructor
public class CertificateDaoImpl implements CertificateDao {

    @PersistenceUnit
    private final EntityManagerFactory factory;

    @Override
    public Optional<Certificate> getById(final Long id) {
        return Optional.ofNullable(findById(id));
    }

    @Override
    public Optional<Certificate> getByName(
            final String name) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            List<Certificate> certificates = entityManager
                    .createQuery(SELECT_BY_NAME, Certificate.class)
                    .setParameter("name", name)
                    .getResultList();

            return certificates.isEmpty()
                    ? Optional.empty()
                    : Optional.of(certificates.get(0));
        }
    }

    @Override
    public List<Certificate> getAll(Pageable pageable) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Certificate> query = builder.createQuery(Certificate.class);
            Root<Certificate> root = query.from(Certificate.class);

            root.fetch("tags", JoinType.LEFT);

            query.select(root);

            return entityManager.createQuery(query)
                    .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        }
    }

    @Override
    public Certificate findById(final Long id) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            return entityManager.find(Certificate.class, id);
        }
    }

    @Override
    public Certificate save(final Certificate certificate) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                Set<Tag> tagSet = certificate.getTags()
                        .stream().map(tag -> entityManager
                                .createQuery(SELECT_TAGS_BY_NAME, Tag.class)
                                .setParameter("name", tag.getName())
                                .getResultList()
                                .stream()
                                .findFirst()
                                .orElse(tag))
                        .collect(toSet());
                certificate.setTags(tagSet);
                Certificate saved = entityManager.merge(certificate);
                transaction.commit();
                return saved;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new CertificateNotFoundException(e.getMessage());
            }
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                Certificate entity = entityManager.getReference(Certificate.class, id);
                entityManager.remove(entity);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new PersistenceException(e.getMessage(), e);
            }
        }
    }

    @Override
    public Certificate update(final Certificate certificate) {
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
                existed.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
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

    @Override
    public List<TagDto> findTagsByCertificateId(final Long id) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            return entityManager.createQuery(SELECT_TAGS_BY_ID, TagDto.class)
                    .setParameter("id", id)
                    .getResultList();
        }
    }

    @Override
    public List<Certificate> findByTagNames(final List<String> tagNames) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Certificate> query = builder.createQuery(Certificate.class);
            Root<Certificate> root = query.from(Certificate.class);
            query.select(root).where(builder.and(tagNames.stream()
                    .map(tagName -> builder
                            .equal(root.join("tags", JoinType.INNER).get("name"), tagName))
                    .toArray(Predicate[]::new)));
            return entityManager.createQuery(query).getResultList();
        }
    }

    @Override
    public List<Certificate> getCertificatesByUserId(
            final Long id) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            return entityManager
                    .createQuery(SELECT_CERTIFICATES_BY_USER_ID, Certificate.class)
                    .setParameter("id", id)
                    .getResultList();
        }
    }

    public Set<Certificate> findAllByIds(Set<Long> certificateIds) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityGraph<Certificate> graph = entityManager.createEntityGraph(Certificate.class);
            graph.addAttributeNodes("tags");
            return new HashSet<>(entityManager
                    .createQuery(SELECT_ALL_BY_IDS, Certificate.class)
                    .setParameter("ids", certificateIds)
                    .setHint("jakarta.persistence.fetchgraph", graph)
                    .getResultList());
        }
    }
}
