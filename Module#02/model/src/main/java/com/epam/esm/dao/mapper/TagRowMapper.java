package com.epam.esm.dao.mapper;

import com.epam.esm.domain.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

@Component
public class TagRowMapper implements RowMapper<Tag> {
    private final String TAG_ID = "t_id";
    private final String TAG_NAME = "t_name";

    @Override
    public Tag mapRow(final ResultSet resultSet, final int i)
            throws SQLException {
        return new Tag(
                resultSet.getLong(TAG_ID),
                resultSet.getString(TAG_NAME),
                new HashSet<>());
    }
}
