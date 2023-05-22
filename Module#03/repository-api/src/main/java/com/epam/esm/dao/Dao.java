package com.epam.esm.dao;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Dao<T extends Serializable> {
    List<T> getAll(Pageable pageable);

    Optional<T> getById(Long id);

    Optional<T> getByName(String name);

    T save(T entity);

    void delete(Long id);
}
