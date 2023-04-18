package com.epam.esm.dao;

import com.epam.esm.criteria.QueryBuilder;
import com.epam.esm.entity.Certificate;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.mapper.CertificateListExtractor;
import com.epam.esm.mapper.CertificateRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.epam.esm.mapper.QueriesContext.*;

@Repository
@RequiredArgsConstructor
public class CertificateDaoImpl implements CertificateDao {

    private final JdbcTemplate jdbcTemplate;
    private final CertificateRowMapper certificateRowMapper;
    private final CertificateListExtractor listExtractor;

    @Override
    public final Optional<Certificate> getById(final Long id) {
        List<Certificate> certificates = jdbcTemplate.query(
                GET_CERTIFICATE_BY_ID,
                new Object[]{id},
                listExtractor);
        return certificates == null
                || certificates.isEmpty()
                ? Optional.empty()
                : Optional.of(certificates.get(0));
    }

    @Override
    public final Optional<Certificate> getByName(final String name) {
        List<Certificate> certificates = jdbcTemplate.query(
                String.format("%s'%s'", GET_CERTIFICATE_BY_NAME, name),
                certificateRowMapper);
        return certificates.isEmpty()
                ? Optional.empty()
                : Optional.of(certificates.get(0));
    }

    @Override
    public final List<Certificate> getAll() {
        return jdbcTemplate.query(
                GET_ALL_CERTIFICATE,
                certificateRowMapper);
    }

    @Override
    public final List<Certificate> getAllBy(final Criteria criteria) {
        return jdbcTemplate.query(
                QueryBuilder.builder().searchBy(criteria).build(),
                listExtractor);
    }

    @Override
    public final Long save(final Certificate certificate) {
        long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        jdbcTemplate.update(INSERT_CERTIFICATE,
                id,
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
        return id;
    }

    @Override
    public final void delete(final Long id) {
        jdbcTemplate.update(DELETE_REF, id);
        jdbcTemplate.update(DELETE_CERTIFICATE, id);
    }

    @Override
    public final boolean update(final Certificate certificate) {
        return jdbcTemplate.update(
                QueryBuilder.builder()
                        .updateQuery(certificate, UPDATE_CERTIFICATE)
                        .build()) == 1;
    }
}
