package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "tags")
public class DefaultTagService implements TagService {

    private final TagDao tagDao;
    private final TagMapper tagMapper;

    @Override
    @Cacheable
    @Transactional(readOnly = true)
    public TagDto getById(
            final Long id)
            throws RuntimeException {
        return tagMapper.toDto(
                tagDao.getById(id));
    }

    @Override
    @Cacheable
    @Transactional(readOnly = true)
    public TagDto getByName(
            final String name)
            throws RuntimeException {
        return tagMapper.toDto(
                tagDao.getByName(name));
    }

    @Override
    @Cacheable
    @Transactional(readOnly = true)
    public List<TagDto> getAll()
            throws RuntimeException {
        return tagDao.getAll()
                .stream()
                .map(tagMapper::toDto)
                .collect(toList());
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean save(
            final TagDto tagDto)
            throws RuntimeException {
        return tagDao.save(
                tagMapper.toEntity(tagDto));
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean delete(
            final Long id)
            throws RuntimeException {
        return tagDao.delete(id);
    }
}
