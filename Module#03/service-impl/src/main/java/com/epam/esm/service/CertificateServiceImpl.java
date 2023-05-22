package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.mapper.CertificateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
        Certificate certificate = certificateDao.getById(id)
                .orElseThrow(() -> new CertificateNotFoundException(
                        String.format("%s id: %d", MESSAGE, id)));
        return mapper.toDto(certificate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CertificateDto> getAll(final Pageable pageable) {
        List<CertificateDto> dtos = mapper.toDtoList(certificateDao.getAll(pageable));
        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateDto getByName(
            final String name) {
        Objects.requireNonNull(name, "Name should be not null");
        Certificate certificate = certificateDao.getByName(name)
                .orElseThrow(() -> new CertificateNotFoundException(
                        String.format("%s name: %s", MESSAGE, name)));
        return mapper.toDto(certificate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "Id should be not null");
        certificateDao.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CertificateDto update(
            final CertificateDto dto) {
        Certificate updated = certificateDao.update(
                mapper.toEntity(dto));
        return mapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CertificateDto> getAllWithoutTags(final Pageable pageable) {
        List<CertificateDto> dtos = mapper.toDtoList(certificateDao.getAll(pageable));
        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    @Override
    @Transactional(
            rollbackFor = Exception.class,
            isolation = Isolation.READ_COMMITTED)
    public CertificateDto save(final CertificateDto dto) {
        Certificate saved = certificateDao.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDto> findTagsByCertificateId(final Long id) {
        return certificateDao.findTagsByCertificateId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CertificateDto> findCertificatesByTags(
            final List<String> tagNames) {
        Pageable pageable = PageRequest.of(0, 25, Sort.by("name").ascending());
        List<CertificateDto> dtos = tagNames
                != null
                ? mapper.toDtoList(certificateDao.findByTagNames(tagNames))
                : mapper.toDtoList(certificateDao.getAll(pageable));
        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    @Override
    public Page<CertificateDto> getCertificatesByUserId(
            final Long id) {
        List<CertificateDto> dtos = mapper.toDtoList(
                certificateDao.getCertificatesByUserId(id));
        return new PageImpl<>(dtos, Pageable.unpaged(), dtos.size());
    }

    @Override
    public Page<CertificateDto> getByIds(
            final Set<Long> ids) {
        List<CertificateDto> dtos = mapper.toDtoList(
                new ArrayList<>(certificateDao.findAllByIds(ids)));
        return new PageImpl<>(dtos, Pageable.unpaged(), dtos.size());
    }
}
