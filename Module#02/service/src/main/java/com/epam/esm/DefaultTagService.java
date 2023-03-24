package com.epam.esm;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.mapper.TagDtoMapper;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ServiceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class DefaultTagService implements TagService {

    private final TagDao tagDao;
    private final TagDtoMapper tagDtoMapper;

    @Override
    public TagDto getBy(final Long id) {
        Tag tag = tagDao.getById(id).orElseThrow(() ->
                new ServiceException(
                        String.format("Certificate not found%d", id)));
        return tagDtoMapper.toDto(tag);
    }

    @Override
    public List<TagDto> getAll() {
        return tagDao.getAll()
                .stream()
                .map(tagDtoMapper::toDto)
                .collect(toList());
    }

}
