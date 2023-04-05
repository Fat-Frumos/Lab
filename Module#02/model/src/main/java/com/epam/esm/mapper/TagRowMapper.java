package com.epam.esm.mapper;

import com.epam.esm.domain.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper<Tag> {
    private static final String TAG_ID = "tag_id";
    private static final String TAG_NAME = "tag_name";

    @Override
    public Tag mapRow(
            final ResultSet resultSet, final int i)
            throws SQLException {
        return Tag.builder()
                .id(resultSet.getLong(TAG_ID))
                .name(resultSet.getString(TAG_NAME))
                .build();
    }
}
