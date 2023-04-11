package com.epam.esm.mapper;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class CertificateListExtractor implements ResultSetExtractor<List<Certificate>> {
    private static final String GIFT_ID = "id";
    private final TagRowMapper tagRowMapper;
    private final CertificateRowMapper rowMapper;

    @Override
    public List<Certificate> extractData(final ResultSet resultSet)
            throws SQLException, DataAccessException {
        Map<Long, Certificate> map = new LinkedHashMap<>();
        while (resultSet.next()) {
            long certificateId = resultSet.getLong(GIFT_ID);
            Certificate certificate = map.get(certificateId);
            if (certificate == null) {
                certificate = rowMapper.mapRow(resultSet, 0);
                Objects.requireNonNull(certificate).setTags(new HashSet<>());
                map.put(certificateId, certificate);
            }
            Tag tag = tagRowMapper.mapRow(resultSet, resultSet.getMetaData().getColumnCount());
            certificate.getTags().add(tag);
        }
        return new ArrayList<>(map.values());
    }
}
