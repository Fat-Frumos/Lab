package com.epam.esm.service;

import java.util.List;

public interface BaseService<T> {

    T getById(Long id);

    List<T> getAll();

    T getByName(String name);

    boolean save(T t);

    boolean delete(Long id);
}
