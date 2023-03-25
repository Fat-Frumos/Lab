package com.epam.esm;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class DefaultTagService implements TagService {

    private final TagDao tagDao;

    @Override
    @Transactional(readOnly = true)
    public TagDto getById(final Long id)
            throws ServiceException {
        try {
            Tag tag = tagDao.getById(id);
            return new TagDto(tag);
        } catch (DaoException e) {
            throw new ServiceException(
                    "Certificate not found", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDto> getAll()
            throws ServiceException {
        try {
            return tagDao.getAll()
                    .stream()
                    .map(TagDto::new)
                    .collect(toList());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
