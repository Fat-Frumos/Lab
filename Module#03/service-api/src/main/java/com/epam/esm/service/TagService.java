package com.epam.esm.service;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.dto.TagDto;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface TagService {
    TagDto getById(Long id);

    TagDto getByName(String name);

    List<TagDto> getAll(Criteria criteria);

    TagDto save(TagDto tag);

    void delete(Long id);
}
