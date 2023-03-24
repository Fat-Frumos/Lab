package com.epam.esm.dao;

import com.epam.esm.dao.mapper.CertificateRowMapper;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.CertificateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.dao.mapper.QueriesContext.*;

@Repository
@RequiredArgsConstructor
public class DefaultCertificateDao implements CertificateDao {

    private final JdbcTemplate jdbcTemplate;
    private final CertificateRowMapper certificateRowMapper;

    @Override
    public final Optional<Certificate> getById(final Long id) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(GET_BY_ID, new Object[]{id},
                        certificateRowMapper));
    }

    @Override
    public final Optional<Tag> getByName(final String name) {
        return Optional.empty();
    }

    @Override
    public final List<Certificate> getAll() {
        return jdbcTemplate.query(
                GET_ALL_CERTIFICATE,
                certificateRowMapper);
    }

    @Override
    public final Certificate save(final Certificate certificate) {
        certificate.setId((System.currentTimeMillis() >> 48) & 0x0FFF);
        jdbcTemplate.update(INSERT_CERTIFICATE,
                certificate.getId(),
                certificate.getName(),
                certificate.getDescription(),
                Timestamp.from(certificate.getCreateDate()),
                Timestamp.from(certificate.getLastUpdateDate()),
                certificate.getDuration());
        return certificate;
    }

    @Override
    public final void delete(final Long id) {

        this.jdbcTemplate.update(DELETE_CERTIFICATE, id);
    }


    @Override
    public final CertificateDto update(
            final Certificate certificate) {
        //TODO
        return new CertificateDto();
    }
}
