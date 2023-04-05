package com.epam.esm.dao;

import com.epam.esm.criteria.QueryBuilder;
import com.epam.esm.domain.Certificate;
import com.epam.esm.criteria.Criteria;
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
    public final Certificate getById(final Long id) throws RuntimeException {
        List<Certificate> result = jdbcTemplate.query(
                GET_CERTIFICATE_BY_ID,
                new Object[]{id},
                listExtractor);
        if (result == null || result.isEmpty()) {
            throw new RuntimeException(
                    String.format("Certificate not found with id: %d", id));
        }
        return result.get(0);
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
    public final List<Certificate> getAllBy(Criteria criteria)
            throws RuntimeException { // TODO test with criteria
        log.info(criteria.toString());
        return jdbcTemplate.query(
                GET_ALL_WITH_TAG_ID,
                listExtractor);
    }

    @Override
    public final boolean save(
            final Certificate certificate)
            throws RuntimeException {
        return jdbcTemplate.update(INSERT_CERTIFICATE,
                System.currentTimeMillis() >> 48 & 0x0FFF,
                certificate.getName(),
                certificate.getDescription(),
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()),
                certificate.getDuration()) == 1;
    }

    @Override
    public final boolean delete(final Long id)
            throws RuntimeException {
        if (getById(id) != null) {
            return jdbcTemplate.update(
                    DELETE_CERTIFICATE, id) == 1;
        } else {
            throw new RuntimeException(
                    String.format("Certificate not found with id: %d", id));
        }
    }

    @Override
    public final boolean update(
            final Certificate certificate)
            throws RuntimeException {
        return jdbcTemplate.update(
                QueryBuilder.builder().updateQuery(
                        certificate,
                        UPDATE_CERTIFICATE)
                        .toString()) == 1;
    }
}
