package com.epam.esm.dao;

import com.epam.esm.criteria.CertificateQueries;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.FilterParams;
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
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static javax.swing.SortOrder.ASCENDING;

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
        try (Session session = entityManagerFactory
                .unwrap(SessionFactory.class).openSession()) {
            return session
                    .createQuery(CertificateQueries.SELECT_ALL_WITH_TAGS, Certificate.class)
                    .getResultList();
        }
    }

    @Override
    public List<Certificate> getAllBy(
            final Criteria criteria) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Certificate> query = builder.createQuery(Certificate.class);
            Root<Certificate> root = query.from(Certificate.class);
            if (criteria.getTags() != null && !criteria.getTags().isEmpty()) {
                Join<Certificate, Tag> join = root.join("tags", JoinType.INNER);
                query.where(join.in(criteria.getTags()));
            }

            if (criteria.getSortOrder() != null && criteria.getFilterParams() != null) {
                String sortBy = criteria.getFilterParams().name().toLowerCase();
                Expression<?> sortByExpression = root.get(sortBy);
                query.orderBy(criteria.getSortOrder() == ASCENDING
                        ? builder.asc(sortByExpression)
                        : builder.desc(sortByExpression));
            }

            TypedQuery<Certificate> typedQuery = entityManager.createQuery(query);
            if (criteria.getParamsMap().containsKey(FilterParams.PAGE)
                    && criteria.getParamsMap().containsKey(FilterParams.SIZE)) {
                Long page = (Long) criteria.getParamsMap().get(FilterParams.PAGE);
                Long size = (Long) criteria.getParamsMap().get(FilterParams.SIZE);
                typedQuery.setFirstResult((page.intValue() - 1) * size.intValue());
                typedQuery.setMaxResults(size.intValue());
            }
            return typedQuery.getResultList();
        }
    }

    @Override
    public Certificate findById(final Long id) {
        try (Session session = entityManagerFactory
                .unwrap(SessionFactory.class).openSession()) {
            return session.get(Certificate.class, id);
        }
    }

    @Override
    public Certificate save(Certificate certificate) {
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
            transaction.commit();
            return saved;
        }
    }

    @Override
    public void delete(final Long id) {
        try (Session session = entityManagerFactory
                .unwrap(SessionFactory.class).openSession()) {
            Certificate certificate = session.get(Certificate.class, id);
            session.getTransaction().begin();
            if (certificate == null) {
                session.getTransaction().rollback();
                throw new EntityNotFoundException("Certificate not found");
            }
            session.remove(certificate);
            session.getTransaction().commit();
        }
    }

    @Override
    public Certificate update(
            final Certificate certificate,
            final Long id) {
        try (Session session = entityManagerFactory
                .unwrap(SessionFactory.class).openSession()) {
            Certificate existed = session.get(Certificate.class, id);
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
            session.beginTransaction();
            session.persist(existed);
            session.getTransaction().commit();
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
}