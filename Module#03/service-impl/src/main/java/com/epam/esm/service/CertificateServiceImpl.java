package com.epam.esm.service;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.CertificateAlreadyExistsException;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.mapper.CertificateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private static final String MESSAGE = "Certificate not found with";
    private final CertificateDao certificateDao;
    private final CertificateMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public CertificateDto getById(final Long id) {
        Objects.requireNonNull(id, "Id should be not null");
        return mapper.toDto(certificateDao.getById(id)
                .orElseThrow(() -> new CertificateNotFoundException(
                        String.format("%s id: %d", MESSAGE, id))));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateDto> getAll(
            final Criteria criteria) {
        return mapper.toDtoList(
                certificateDao.getAll(criteria));
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateDto getByName(final String name) {
        Objects.requireNonNull(name, "Name should be not null");
        return mapper.toDto(certificateDao.getByName(name)
                .orElseThrow(() -> new CertificateNotFoundException(
                        String.format("%s name: %s", MESSAGE, name))));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "Id should be not null");
        if (getById(id) == null) {
            throw new CertificateNotFoundException(
                    String.format("%s id: %d", MESSAGE, id));
        }
        certificateDao.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CertificateDto update(
            final CertificateDto dto,
            final Long id) {
        if (certificateDao.getById(id).isPresent()) {
            certificateDao.update(mapper.toEntity(dto), id);
            return getById(dto.getId());
        } else {
            throw new CertificateNotFoundException(
                    String.format("%s id: %d", MESSAGE, dto.getId()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateWithoutTagDto> getAllWithoutTags(
            final Criteria criteria) {
        return mapper.toDtoWithoutTagsList(
                certificateDao.getAll(criteria));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public CertificateDto save(final CertificateDto dto) {
        if (certificateDao.getByName(dto.getName()).isPresent()) {
            throw new CertificateAlreadyExistsException(dto.getName());
        }
        return mapper.toDto(certificateDao.save(mapper.toEntity(dto)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDto> findTagsByCertificateId(
            final Long id) {
        return certificateDao.findTagsByCertificateId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateDto> findCertificatesByTags(
            final List<String> tagNames) {
        return tagNames != null
                ? mapper.toDtoList(certificateDao.findByTagNames(tagNames))
                : mapper.toDtoList(certificateDao.getAll(Criteria.builder().page(0).size(25).build()));
    }

    @Override
    public List<CertificateDto> getCertificatesByUserId(
            final Long id) {
        return certificateDao.getCertificatesByUserId(id)
                .stream()
                .map(mapper::toDto)
                .collect(toList());
    }

    @Override
    public Set<CertificateDto> getByIds(
            final Set<Long> ids) {
        return new HashSet<>(mapper.toDtoList(
                new ArrayList<>(certificateDao.findAllByIds(ids))));
    }
}
