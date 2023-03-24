package com.epam.esm;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.mapper.CertificateDtoMapper;
import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class DefaultCertificateService implements CertificateService {

    private final CertificateDao certificateDao;
    private final CertificateDtoMapper certificateDtoMapper;

    @Override
    public CertificateDto getById(final Long id) {
        Certificate certificate =
                certificateDao.getById(id).orElseThrow(() ->
                        new ServiceException(String.format("Certificate not found%d", id)));
        return certificateDtoMapper.toDto(certificate);
    }

    @Override
    public List<CertificateDto> getAll() {
        return certificateDao.getAll()
                .stream()
                .map(certificateDtoMapper::toDto)
                .collect(toList());
    }

    @Transactional
    @Override
    public boolean delete(final Long id) {
        //TODO
        return false;

    }

    @Transactional
    @Override
    public Certificate update(final CertificateDto certificateDto) {
        //TODO
        return new Certificate();
    }
}
