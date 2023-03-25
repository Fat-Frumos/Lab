package com.epam.esm;

import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.ServiceException;

import java.util.List;

public interface CertificateService {

    CertificateDto getById(Long id) throws ServiceException;

    CertificateDto getByName(String name) throws ServiceException;

    List<CertificateDto> getAll() throws ServiceException;

    Certificate update(CertificateDto certificateDto) throws ServiceException;

    boolean delete(Long id) throws ServiceException;
}
