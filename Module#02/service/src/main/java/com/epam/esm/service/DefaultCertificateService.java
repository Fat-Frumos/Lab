package com.epam.esm.service;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.exception.CertificateIsExistsException;
import com.epam.esm.exception.CertificateNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.epam.esm.mapper.CertificateMapper.mapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCertificateService implements CertificateService {

    private final CertificateDao certificateDao;

    @Override
    @Transactional(readOnly = true)
    public CertificateDto getById(final Long id) {
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
    public boolean delete(final Long id) {
        return certificateDao.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateDto> getAllBy(final Criteria criteria) {
        return mapper.toDtoList(certificateDao.getAllBy(criteria));
    }

    @Override
    @Transactional
    public boolean update(final CertificateDto certificateDto) {
        if (getById(certificateDto.getId()) != null) {
            throw new CertificateNotFoundException(
                    String.format("Certificate# %d not found", certificateDto.getId()));
        } else {
            return certificateDao.update(mapper.toEntity(certificateDto));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateWithoutTagDto> getAllWithoutTags() {
        return mapper.toDtoWithoutTagsList(certificateDao.getAll());
    }

    @Override
    @Transactional
    public boolean save(final CertificateDto certificateDto) {
        if (certificateDao.getByName(certificateDto.getName()) != null) {
            throw new CertificateIsExistsException(certificateDto.getName());
        } else {
            return certificateDao.save(mapper.toEntity(certificateDto));
        }
    }
}
