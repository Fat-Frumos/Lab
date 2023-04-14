package com.epam.esm.service;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.exception.CertificateAlreadyExistsException;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.mapper.CertificateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDao certificateDao;
    private final CertificateMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public CertificateDto getById(final Long id) {
        Objects.requireNonNull(id, "Id should be not null");
        return mapper.toDto(certificateDao.getById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateDto> getAll() {
        return mapper.toDtoList(certificateDao.getAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateDto getByName(final String name) {
        return mapper.toDto(certificateDao.getByName(name));
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        if (getById(id) != null) {
            certificateDao.delete(id);
        } else {
            throw new CertificateNotFoundException(
                    String.format("Certificate not found with id: %d", id));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateDto> getAllBy(final Criteria criteria) {
        return mapper.toDtoList(certificateDao.getAllBy(criteria));
    }

    @Override
    @Transactional
    public CertificateDto update(final CertificateDto certificateDto) {
        if (getById(certificateDto.getId()) != null) {
             certificateDao.update(mapper.toEntity(certificateDto));
             return getById(certificateDto.getId());
        } else {
            throw new CertificateNotFoundException(
                    String.format("Certificate# %d not found",
                            certificateDto.getId()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateWithoutTagDto> getAllWithoutTags() {
        return mapper.toDtoWithoutTagsList(certificateDao.getAll());
    }

    @Override
    @Transactional
    public CertificateDto save(final CertificateDto certificateDto) {
        if (certificateDao.getByName(certificateDto.getName()) != null) {
            throw new CertificateAlreadyExistsException(certificateDto.getName());
        } else {
            Long saved = certificateDao.save(mapper.toEntity(certificateDto));
            return getById(saved);
        }
    }
}
