package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;

import java.util.List;

public interface CertificateService extends BaseService<CertificateDto> {
    @Override
    CertificateDto getById(Long id) throws RuntimeException;

    @Override
    CertificateDto getByName(String name) throws RuntimeException;

    @Override
    List<CertificateDto> getAll() throws RuntimeException;

    boolean update(CertificateDto certificate) throws RuntimeException;

    @Override
    boolean save(CertificateDto certificate) throws RuntimeException;

    @Override
    boolean delete(Long id) throws RuntimeException;
}
