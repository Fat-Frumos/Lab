package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.mapper.CertificateMapper;
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
@CacheConfig(cacheNames = "certificates")
public class DefaultCertificateService implements CertificateService {

    private final CertificateDao certificateDao;
    private final CertificateMapper mapper;

    @Override
    @Cacheable
    @Transactional(readOnly = true)
    public CertificateDto getById(final Long id)
            throws RuntimeException {
        return mapper.toDto(
                certificateDao.getById(id));
    }

    @Override
    @Cacheable
    @Transactional(readOnly = true)
    public List<CertificateDto> getAll()
            throws RuntimeException {
        return certificateDao.getAll().stream()
                .map(mapper::toDto)
                .collect(toList());
    }

    @Override
    @Cacheable
    @Transactional(readOnly = true)
    public CertificateDto getByName(
            final String name)
            throws RuntimeException {
        return mapper.toDto(
                certificateDao.getByName(name));
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean delete(
            final Long id)
            throws RuntimeException {
        return certificateDao.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateDto> getAllBy(Criteria criteria)
            throws RuntimeException {
        return certificateDao
                .getAllBy(criteria).stream()
                .map(mapper::toDto)
                .collect(toList());
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean update(
            final CertificateDto certificateDto)
            throws RuntimeException {
        return certificateDao.update(
                mapper.toEntity(certificateDto));
    }

    @Override
    public List<CertificateWithoutTagDto> getAllWithoutTags()
           throws RuntimeException {
            return certificateDao.getAll().stream()
                    .map(mapper::toDtoWithoutTags)
                    .collect(toList());
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean save(
            final CertificateDto certificateDto)
            throws RuntimeException {
        return certificateDao.save(
                mapper.toEntity(certificateDto));
    }
}
