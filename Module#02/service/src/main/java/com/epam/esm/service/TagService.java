package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

public interface TagService extends BaseService<TagDto> {

    @Override
    TagDto getById(Long id);

    @Override
    TagDto getByName(String name);

    @Override
    List<TagDto> getAll();

    @Override
    boolean save(TagDto tag);

    @Override
    boolean delete(Long id);
}
