package com.epam.esm.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Dao<T extends Serializable> {
        List<T> getAll();

        Optional<T> getById(Long id);

        Optional<T> getByName(String name);

        Long save(T entity);

        void delete(Long id);
}