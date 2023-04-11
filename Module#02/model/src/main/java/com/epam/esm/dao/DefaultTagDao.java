package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.mapper.TagRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.epam.esm.mapper.QueriesContext.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DefaultTagDao implements TagDao {

    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper tagRowMapper;

    @Override
    public final Tag getById(final Long id) {
        return jdbcTemplate.queryForObject(GET_TAG_BY_ID, new Object[]{id}, tagRowMapper);
    }

    @Override
    public final Tag getByName(final String name) {
        String sql = String.format("%s'%s'", GET_BY_TAG_NAME, name);
        List<Tag> tags = jdbcTemplate.query(sql, tagRowMapper);
        return tags.isEmpty() ? null : tags.get(0);
    }

    @Override
    public final List<Tag> getAll() {
        return jdbcTemplate.query(GET_ALL_TAGS, tagRowMapper);
    }

    @Override
    public final boolean save(final Tag tag) {
        return jdbcTemplate.update(INSERT_TAG, tag.getName()) == 1;
    }

    @Override
    public final boolean delete(final Long id) {
        if (getById(id) != null) {
            jdbcTemplate.update(DELETE_TAG_REF, id);
            return jdbcTemplate.update(DELETE_TAG, id) == 1;
        } else {
            throw new TagNotFoundException(
                    String.format("Tag not found with id: %d", id));
        }
    }
}
