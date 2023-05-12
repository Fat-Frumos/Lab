package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Repository
@RequiredArgsConstructor
public class TagDaoImpl implements TagDao {

    @PersistenceUnit
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Optional<Tag> getById(final Long id) {
        try {
            return Optional.ofNullable(findById(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Tag> getByName(String name) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
            query.where(builder.equal(query.from(Tag.class).get("name"), name));
            TypedQuery<Tag> typedQuery = entityManager.createQuery(query);
            typedQuery.setMaxResults(1);
            List<Tag> tags = typedQuery.getResultList();
            return tags.isEmpty() ? Optional.empty() : Optional.of(tags.get(0));
        }
    }

    @Override
    public List<Tag> getAll() {
        try (EntityManager entityManager
                     = entityManagerFactory.createEntityManager()) {
            CriteriaQuery<Tag> query = entityManager
                    .getCriteriaBuilder()
                    .createQuery(Tag.class);
            query.select(query.from(Tag.class));
            return entityManager.createQuery(query)
                    .getResultList();
        }
    }

    @Override
    public Tag save(final Tag tag) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(tag);
            transaction.commit();
            return tag;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new EntityExistsException(e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Tag findById(final Long id) {
        try (EntityManager entityManager =
                     entityManagerFactory.createEntityManager()) {
            return entityManager.find(Tag.class, id);
        }
    }

    @Override
    public void delete(final Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Tag tag = entityManager.find(Tag.class, id);
            if (tag == null) {
                throw new EntityNotFoundException(String.format("Tag with id %d not found", id));
            }
            entityManager.remove(tag);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new EntityNotFoundException(e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Set<Tag> saveAll(
            final Set<Tag> tags) {
        return tags.stream()
                .map(this::save)
                .collect(toSet());
    }
}
