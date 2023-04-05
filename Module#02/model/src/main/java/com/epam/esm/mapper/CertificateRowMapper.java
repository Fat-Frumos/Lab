package com.epam.esm.mapper;

import com.epam.esm.domain.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CertificateRowMapper implements RowMapper<Certificate> {

    private static final String GIFT_ID = "id";
    private static final String GIFT_NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String DURATION = "duration";
    private static final String PRICE = "price";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";

    @Override
    public Certificate mapRow(
            final ResultSet resultSet,
            final int i)
            throws SQLException {

        return Certificate.builder()
                .id(resultSet.getLong(GIFT_ID))
                .name(resultSet.getString(GIFT_NAME))
                .description(resultSet.getString(DESCRIPTION))
                .duration(resultSet.getInt(DURATION))
                .price(resultSet.getBigDecimal(PRICE))
                .createDate(resultSet.getTimestamp(CREATE_DATE).toInstant())
                .lastUpdateDate(resultSet.getTimestamp(LAST_UPDATE_DATE).toInstant())
                .build();
    }
}
