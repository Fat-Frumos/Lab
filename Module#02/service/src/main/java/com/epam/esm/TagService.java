package com.epam.esm;

import com.epam.esm.dto.TagDto;

import java.util.List;

public interface TagService {

    TagDto getBy(Long id);

    List<TagDto> getAll();

}
