package com.epam.esm.dao;

import java.io.Serializable;
import java.util.List;

public interface Dao<T extends Serializable> {
    List<T> getAll();

    T getById(Long id);

    T getByName(String name);

    boolean save(T entity);

    boolean delete(Long id);
}
