package com.epam.esm.dao;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.FilterParams;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CertificateNotFoundException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sqm.SortOrder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.criteria.CertificateQueries.*;

@Repository
@RequiredArgsConstructor
public class CertificateDaoImpl implements CertificateDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Certificate> getById(final Long id) {
        return Optional.ofNullable(findById(id));
    }

    @Override
    public Optional<Certificate> getByName(
            final String name) {
        List<Certificate> certificates = entityManager
                .createQuery(
                        SELECT_BY_NAME,
                        Certificate.class)
                .setParameter("name", name)
                .getResultList();
        return certificates.isEmpty()
                ? Optional.empty()
                : Optional.of(certificates.get(0));
    }

    @Override
    public List<Certificate> getAll() {
        return entityManager
                .createQuery(SELECT_ALL_WITH_TAGS, Certificate.class)
                .getResultList();
    }


    @Override
    public List<Certificate> getAllBy(final Criteria criteria) {
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
            query.orderBy(criteria.getSortOrder() == SortOrder.ASCENDING
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

    @Override
    public Certificate findById(final Long id) {
        return entityManager
                .find(Certificate.class, id);
    }

    @Override
    public Certificate save(
            final Certificate certificate) {
        try {
            return entityManager.merge(certificate);
        } catch (Exception e) {
            throw new EntityExistsException(e.getMessage());
        }
    }

    @Override
    public void delete(final Long id) {
        entityManager.remove(findById(id));
    }

    @Override
    public Certificate update(
            final Certificate certificate,
            final Long id) {
        Certificate existed = entityManager
                .find(Certificate.class, id);

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
        entityManager.merge(existed);
        return existed;
    }

    @Override
    public List<TagDto> findTagsByCertificateId ( //TODO
            final Long id) {
        return entityManager
                .createQuery(SELECT_TAGS_BY_ID, TagDto.class)
                .setParameter("id", id)
                .getResultList();
    }
}
