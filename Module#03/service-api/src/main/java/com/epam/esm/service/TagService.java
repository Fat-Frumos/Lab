package com.epam.esm.service;
import org.springframework.data.domain.Pageable;
import com.epam.esm.dto.TagDto;

import java.util.List;

public interface TagService {
    TagDto getById(Long id);

    TagDto getByName(String name);

    List<TagDto> getAll(Pageable pageable);

    TagDto save(TagDto tag);

    void delete(Long id);
}
