package com.epam.esm;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.DaoException;
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

    @Override
    @Transactional(readOnly = true)
    public CertificateDto getById(final Long id)
            throws ServiceException {
        try {
            return new CertificateDto(
                    certificateDao.getById(id));
        } catch (DaoException e) {
            throw new ServiceException(
                    "Certificate %d not found", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateDto> getAll()
            throws ServiceException {
        try {
            return certificateDao.getAll()
                    .stream()
                    .map(CertificateDto::new)
                    .collect(toList());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
    @Override
    @Transactional(readOnly = true)
    public CertificateDto getByName(final String name)
            throws ServiceException {
        try {
            return new CertificateDto(
                    certificateDao.getByName(name));
        } catch (DaoException e) {
            throw new ServiceException(
                    "Certificate %d not found", e);
        }
    }

    @Override
    @Transactional
    public boolean delete(final Long id)
            throws ServiceException {
        try {
            return certificateDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Certificate update(
            final CertificateDto certificateDto)
            throws ServiceException {
        //TODO
        return new Certificate();
    }
}
