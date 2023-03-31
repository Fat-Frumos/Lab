package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

public interface TagService extends BaseService<TagDto> {

    @Override
    TagDto getById(Long id) throws RuntimeException;

    @Override
    TagDto getByName(String name) throws RuntimeException;

    @Override
    List<TagDto> getAll() throws RuntimeException;

    @Override
    boolean save(TagDto tag) throws RuntimeException;

    @Override
    boolean delete(Long id) throws RuntimeException;
}
