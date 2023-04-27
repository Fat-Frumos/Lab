package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Tag> getById(final Long id) {
        try {
            return Optional.ofNullable(
                    entityManager.find(Tag.class, id));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Tag> getByName(
            final String name) {
        try {
            return Optional.ofNullable(
                    entityManager.find(Tag.class, name));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> getAll() {
        CriteriaQuery<Tag> query = entityManager
                .getCriteriaBuilder()
                .createQuery(Tag.class);
        query.select(query.from(Tag.class));
        return entityManager
                .createQuery(query)
                .getResultList();
    }

    @Override
    public Tag save(final Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public Tag findById(final Long id) {
        return entityManager
                .find(Tag.class, id);
    }

    @Override
    public void delete(final Long id) {
        entityManager
                .remove(findById(id));
    }

    @Override
    public Set<Tag> saveAll(
            final Set<Tag> tags) {
        return tags.stream()
                .map(this::save)
                .collect(toSet());
    }
}
