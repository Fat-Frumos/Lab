package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.TagIsExistsException;
import com.epam.esm.exception.TagNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.epam.esm.mapper.TagMapper.tagMapper;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultTagService implements TagService {

    private final TagDao tagDao;

    @Override
    @Transactional(readOnly = true)
    public TagDto getById(final Long id) {
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
    public boolean save(final TagDto tagDto) {
        if (tagDao.getByName(tagDto.getName()) == null) {
            return tagDao.save(tagMapper.toEntity(tagDto));
        } else {
            throw new TagIsExistsException(tagDto.getName());
        }
    }

    @Override
    @Transactional
    public boolean delete(final Long id) {
        if (tagDao.getById(id) != null) {
            return tagDao.delete(id);
        } else {
            throw new TagNotFoundException(id.toString());
        }
    }
}
