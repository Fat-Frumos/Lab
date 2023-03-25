package com.epam.esm;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ServiceException;

import java.util.List;

public interface TagService {

    TagDto getById(Long id) throws ServiceException;

    List<TagDto> getAll() throws ServiceException;

}
