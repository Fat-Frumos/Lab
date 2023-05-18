package com.epam.esm.dao;

import com.epam.esm.criteria.Criteria;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Dao<T extends Serializable> {
    List<T> getAll(Criteria criteria);

    Optional<T> getById(Long id);

    Optional<T> getByName(String name);

    T save(T entity);

    void delete(Long id);
}
