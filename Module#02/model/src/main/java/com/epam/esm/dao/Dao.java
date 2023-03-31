package com.epam.esm.dao;

import com.epam.esm.domain.BaseEntity;

import java.util.List;

public interface Dao<T extends BaseEntity> {
    List<T> getAll();

    T getById(Long id);

    T getByName(String name);

    boolean save(T entity);

    boolean delete(Long id);
}
