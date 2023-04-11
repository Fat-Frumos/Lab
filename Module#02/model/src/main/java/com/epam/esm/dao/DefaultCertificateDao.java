package com.epam.esm.dao;

import com.epam.esm.criteria.QueryBuilder;
import com.epam.esm.entity.Certificate;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.mapper.CertificateListExtractor;
import com.epam.esm.mapper.CertificateRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static com.epam.esm.mapper.QueriesContext.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DefaultCertificateDao implements CertificateDao {

    private final JdbcTemplate jdbcTemplate;
    private final CertificateRowMapper certificateRowMapper;
    private final CertificateListExtractor listExtractor;

    @Override
    public final Certificate getById(final Long id) {
        List<Certificate> result = jdbcTemplate.query(
                GET_CERTIFICATE_BY_ID,
                new Object[]{id},
                listExtractor);
        if (result == null || result.isEmpty()) {
            throw new CertificateNotFoundException(
                    String.format("Certificate not found with id: %d", id));
        }
        return result.get(0);
    }

    @Override
    public final Certificate getByName(final String name) {
        List<Certificate> certificates = jdbcTemplate.query(
                String.format("%s'%s'", GET_CERTIFICATE_BY_NAME, name),
                certificateRowMapper);
        return certificates.isEmpty() ? null : certificates.get(0);
    }

    @Override
    public final List<Certificate> getAll() {
        return jdbcTemplate.query(
                GET_ALL_CERTIFICATE,
                certificateRowMapper);
    }

    @Override
    public final List<Certificate> getAllBy(
            final Criteria criteria) {
        return jdbcTemplate.query(
                QueryBuilder.builder().searchBy(criteria).build(),
                listExtractor);
    }

    @Override
    public final boolean save(
            final Certificate certificate) {
        return jdbcTemplate.update(INSERT_CERTIFICATE,
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now())) == 1;
    }

    @Override
    public final boolean delete(final Long id) {
        if (getById(id) != null) {
            jdbcTemplate.update(DELETE_REF, id);
            return jdbcTemplate.update(DELETE_CERTIFICATE, id) == 1;
        } else {
            throw new CertificateNotFoundException(
                    String.format("Certificate not found with id: %d", id));
        }
    }

    @Override
    public final boolean update(
            final Certificate certificate) {
        String sql = QueryBuilder.builder().updateQuery(certificate, UPDATE_CERTIFICATE).build();

        log.info(sql);
        return jdbcTemplate.update(sql) == 1;
    }
}
