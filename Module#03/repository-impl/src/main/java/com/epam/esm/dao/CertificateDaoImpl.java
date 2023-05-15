package com.epam.esm.dao;

import com.epam.esm.criteria.CertificateQueries;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CertificateNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Repository
@RequiredArgsConstructor
public class CertificateDaoImpl implements CertificateDao {

    @PersistenceUnit
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Optional<Certificate> getById(final Long id) {
        return Optional.ofNullable(findById(id));
    }

    @Override
    public Optional<Certificate> getByName(String name) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            List<Certificate> certificates = entityManager
                    .createQuery(CertificateQueries.SELECT_BY_NAME, Certificate.class)
                    .setParameter("name", name)
                    .getResultList();
            return certificates.isEmpty()
                    ? Optional.empty()
                    : Optional.of(certificates.get(0));
        }
    }

    @Override
    public List<Certificate> getAll(Criteria criteria) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Certificate> query = builder.createQuery(Certificate.class);
            Root<Certificate> root = query.from(Certificate.class);
            Predicate predicate = builder.conjunction();

            Set<Tag> tags = criteria.getTags();
            if (tags != null) {
                for (Tag tag : criteria.getTags()) {
                    predicate = builder.and(predicate, builder.equal(root
                            .joinList("tags", JoinType.INNER).get("name"), tag.getName()));
                }
            }
            TypedQuery<Certificate> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult((criteria.getPage()) * criteria.getSize());
            typedQuery.setMaxResults(criteria.getSize());
            return typedQuery.getResultList();
        } catch (Exception e) {
            throw new CertificateNotFoundException("Could not find certificate");
        }
    }

    @Override
    public List<Certificate> getAll() {
        try (EntityManager entityManager = entityManagerFactory
                .createEntityManager()) {
            return entityManager
                    .createQuery(CertificateQueries.SELECT_ALL_WITH_TAGS, Certificate.class)
                    .getResultList();
        }
    }

    @Override
    public Certificate findById(final Long id) {
        try (EntityManager entityManager = entityManagerFactory
                .createEntityManager()) {
            return entityManager.find(Certificate.class, id);
        }
    }

    @Override
    public Certificate save(final Certificate certificate) {
        try (EntityManager entityManager = entityManagerFactory
                .createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            certificate.setTags(certificate.getTags()
                    .stream().map(tag -> entityManager
                            .createQuery("SELECT t FROM Tag t WHERE t.name = :name", Tag.class)
                            .setParameter("name", tag.getName())
                            .getResultList()
                            .stream()
                            .findFirst().orElse(tag)).collect(toSet()));

            Certificate saved = entityManager.merge(certificate);
            complete(transaction);
            return saved;
        }
    }

    @Override
    public void delete(final Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.remove(entityManager.find(Certificate.class, id));
            complete(transaction);
        }
    }

    @Override
    public Certificate update(
            final Certificate certificate,
            final Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            Certificate existed = entityManager.find(Certificate.class, id);

            if (existed == null) {
                throw new CertificateNotFoundException(
                        "Certificate not found with id " + id);
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
            entityManager.persist(existed);
            complete(transaction);
            return existed;
        }
    }

    @Override
    public List<TagDto> findTagsByCertificateId(final Long id) {
        try (Session session = entityManagerFactory
                .unwrap(SessionFactory.class).openSession()) {
            TypedQuery<TagDto> query = session
                    .createQuery(CertificateQueries.SELECT_TAGS_BY_ID, TagDto.class);
            query.setParameter("id", id);
            return query.getResultList();
        } catch (Exception e) {
            throw new EntityNotFoundException(
                    "Error while finding tags by certificate id: " + id, e);
        }
    }

    @Override
    public List<Certificate> findByTagNames(List<String> tagNames) {
        try (Session session = entityManagerFactory
                .unwrap(SessionFactory.class).openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
            Root<Certificate> root = criteriaQuery.from(Certificate.class);
            criteriaQuery.select(root)
                    .where(criteriaBuilder.and(tagNames.stream()
                            .map(tagName -> criteriaBuilder.equal(root.join("tags").get("name"), tagName))
                            .collect(toList()).toArray(new Predicate[]{})));
            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public List<Certificate> getCertificatesByUserId(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<Certificate> query = entityManager.createQuery(
                    "SELECT c FROM Certificate c JOIN c.orders o JOIN o.user u WHERE u.id = :id",
                    Certificate.class);
            return query.setParameter("id", id)
                    .getResultList();
        }
    }

    @Override
    public List<Certificate> findAllByIds(List<Long> certificateIds) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<Certificate> query = entityManager.createQuery(
                    "SELECT c FROM Certificate c WHERE c.id IN :ids", Certificate.class);
            return query.setParameter("ids", certificateIds)
                    .getResultList();
        }
    }

    private static void complete(EntityTransaction transaction) {
        Objects.requireNonNull(transaction);
        try {
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new EntityNotFoundException(e.getMessage(), e);
        }
    }
}
