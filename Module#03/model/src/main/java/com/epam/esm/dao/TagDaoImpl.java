package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import jakarta.persistence.EntityExistsException;
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
            return Optional.ofNullable(findById(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Tag> getByName(
            final String name) {
        return entityManager
                .createQuery("SELECT t FROM Tag t WHERE t.name = :name", Tag.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
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
        try {
            entityManager.merge(tag);
            return tag;
        } catch (Exception e) {
            throw new EntityExistsException(e.getMessage());
        }
    }

    @Override
    public Tag findById(final Long id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public void delete(final Long id) {
        entityManager.remove(findById(id));
    }

    @Override
    public Set<Tag> saveAll(
            final Set<Tag> tags) {
        return tags.stream()
                .map(this::save)
                .collect(toSet());
    }
}
