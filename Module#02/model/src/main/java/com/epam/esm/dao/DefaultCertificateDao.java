package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;
import com.epam.esm.mapper.CertificateRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.mapper.QueriesContext.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DefaultCertificateDao implements CertificateDao {

    private final JdbcTemplate jdbcTemplate;
    private final CertificateRowMapper certificateRowMapper;

    @Override
    public final Certificate getById(final Long id)
            throws RuntimeException {
        return jdbcTemplate.queryForObject(
                GET_CERTIFICATE_BY_ID,
                new Object[]{id},
                certificateRowMapper);
    }

    @Override
    public final Certificate getByName(
            final String name)
            throws RuntimeException {
        return jdbcTemplate.queryForObject(
                GET_CERTIFICATE_BY_NAME,
                new Object[]{name},
                certificateRowMapper);
    }

    @Override
    public final List<Certificate> getAll()
            throws RuntimeException {
        return jdbcTemplate.query(
                GET_ALL_CERTIFICATE,
                certificateRowMapper);
    }

    @Override
    public final boolean save(
            final Certificate certificate)
            throws RuntimeException {
        return jdbcTemplate.update(INSERT_CERTIFICATE,
                System.currentTimeMillis() >> 48 & 0x0FFF,
                certificate.getName(),
                certificate.getDescription(),
                Timestamp.from(certificate.getCreateDate()),
                Timestamp.from(certificate.getLastUpdateDate()),
                certificate.getDuration()) == 1;
    }

    @Override
    public final boolean delete(final Long id)
            throws RuntimeException {
        return jdbcTemplate.update(
                DELETE_CERTIFICATE, id) == 1;
    }

    @Override
    public final boolean update(
            final Certificate certificate)
            throws RuntimeException {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(UPDATE_CERTIFICATE);
        if (certificate.getName() != null) {
            sql.append(" SET name = ?, ");
            params.put("name", certificate.getName());
        }
        if (certificate.getDescription() != null) {
            sql.append(" SET description = ?, ");
            params.put("description", certificate.getDescription());
        }
        if (certificate.getPrice() != null) {
            sql.append(" SET price = ?, ");
            params.put("price", certificate.getPrice());
        }
        if (certificate.getDuration() != null) {
            sql.append(" SET duration = ?, ");
            params.put("duration", certificate.getDuration());
        }

        sql.append("lastUpdateDate = ? WHERE id = ?");
        params.put("lastUpdateDate", Instant.now());
        params.put("id", certificate.getId());
        return jdbcTemplate.update(
                sql.toString(), params) == 1;
    }
}
