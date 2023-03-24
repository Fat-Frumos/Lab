package com.epam.esm.dao;

import com.epam.esm.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    List<T> getAll();

    Optional<T> getById(Long id);

    Optional<Tag> getByName(String name);

    T save(T entity);

    void delete(Long id);
}
