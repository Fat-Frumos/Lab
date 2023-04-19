package com.epam.esm.mapper;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for mapping the result set from a database query to a Tag object.
 * <p>
 * It implements the RowMapper interface provided by Spring framework.
 */
@Component
public class TagRowMapper implements RowMapper<Tag> {
    private static final String TAG_ID = "tag_id";
    private static final String TAG_NAME = "tag_name";

    /**
     * Maps a row of the ResultSet to a Tag object.
     *
     * @param resultSet the ResultSet to map
     * @param number    the number of the current row
     * @return the Tag object created from the row
     * @throws SQLException if there is an error in the SQL query
     */
    @Override
    public Tag mapRow(
            final ResultSet resultSet, final int number)
            throws SQLException {
        return Tag.builder()
                .id(resultSet.getLong(TAG_ID))
                .name(resultSet.getString(TAG_NAME))
                .build();
    }
}
