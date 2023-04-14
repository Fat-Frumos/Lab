package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagAlreadyExistsException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;

    private final TagMapper tagMapper;

    @Override
    @Transactional(readOnly = true)
    public TagDto getById(final Long id) {
        Objects.requireNonNull(id, "Id should be not null");
        return tagMapper.toDto(tagDao.getById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public TagDto getByName(final String name) {
        return tagMapper.toDto(tagDao.getByName(name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDto> getAll() {
        return tagDao.getAll()
                .stream()
                .map(tagMapper::toDto)
                .collect(toList());
    }

    @Override
    @Transactional
    public TagDto save(final TagDto tagDto) {
        if (tagDao.getByName(tagDto.getName()) == null) {
            Long saved = tagDao.save(tagMapper.toEntity(tagDto));
            return getById(saved);
        } else {
            throw new TagAlreadyExistsException(tagDto.getName());
        }
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        if (tagDao.getById(id) == null) {
            throw new TagNotFoundException(
                    String.format("Tag not found with id: %d", id));
        } else {
            tagDao.delete(id);
        }
    }
}
