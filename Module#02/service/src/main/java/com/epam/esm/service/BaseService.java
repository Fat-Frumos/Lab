package com.epam.esm.service;

import com.epam.esm.dto.BaseDto;

import java.util.List;

public interface BaseService<T extends BaseDto> {

    T getById(Long id);

    List<T> getAll();

    T getByName(String name);

    boolean save(T t);

    boolean delete(Long id);
}
