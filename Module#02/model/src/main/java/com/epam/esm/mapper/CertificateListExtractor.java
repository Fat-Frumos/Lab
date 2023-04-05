package com.epam.esm.mapper;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
@AllArgsConstructor
public class CertificateListExtractor implements ResultSetExtractor<List<Certificate>> {
    private static final String GIFT_ID = "id";
    public final TagRowMapper tagRowMapper;
    private final CertificateRowMapper rowMapper;

    @Override
    public List<Certificate> extractData(ResultSet resultSet)
            throws SQLException, DataAccessException {
        Map<Long, Certificate> map = new LinkedHashMap<>();
        while (resultSet.next()) {
            long certificateId = resultSet.getLong(GIFT_ID);
            Certificate certificate = map.get(certificateId);
            if (certificate == null) {
                certificate = rowMapper.mapRow(resultSet, 0);
                certificate.setTags(new HashSet<>());
                map.put(certificateId, certificate);
            }
            Tag tag = tagRowMapper.mapRow(resultSet, resultSet.getMetaData().getColumnCount());
            certificate.getTags().add(tag);
        }
        return new ArrayList<>(map.values());
    }
}
