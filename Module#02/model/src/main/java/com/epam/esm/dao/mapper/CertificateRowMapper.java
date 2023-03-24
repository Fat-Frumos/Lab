package com.epam.esm.dao.mapper;

import com.epam.esm.domain.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CertificateRowMapper implements RowMapper<Certificate> {
    private final String GIFT_ID = "id";
    private final String GIFT_NAME = "name";
    private final String DESCRIPTION = "description";
    private final String DURATION = "duration";
    private final String CREATE_DATE = "create_date";
    private final String LAST_UPDATE_DATE = "last_update_date";

    @Override
    public Certificate mapRow(final ResultSet resultSet, final int i) throws SQLException {
        return Certificate.builder()
                .id(resultSet.getLong(GIFT_ID))
                .name(resultSet.getString(GIFT_NAME))
                .description(resultSet.getString(DESCRIPTION))
                .duration(resultSet.getInt(DURATION))
                .createDate(resultSet.getTimestamp(CREATE_DATE).toInstant())
                .lastUpdateDate(resultSet.getTimestamp(LAST_UPDATE_DATE).toInstant())
                .build();
    }
}
