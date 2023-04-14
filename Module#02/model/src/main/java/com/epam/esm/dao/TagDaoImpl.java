package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.TagRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.epam.esm.mapper.QueriesContext.*;

@Repository
@RequiredArgsConstructor
public class TagDaoImpl implements TagDao {

    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper tagRowMapper;

    @Override
    public final Tag getById(final Long id) {
        return jdbcTemplate.queryForObject(
                GET_TAG_BY_ID,
                new Object[]{id},
                tagRowMapper);
    }

    @Override
    public final Tag getByName(final String name) {
        List<Tag> tags = jdbcTemplate.query(
                String.format("%s'%s'", GET_BY_TAG_NAME, name),
                tagRowMapper);
        return tags.isEmpty() ? null : tags.get(0);
    }

    @Override
    public final List<Tag> getAll() {
        return jdbcTemplate.query(GET_ALL_TAGS, tagRowMapper);
    }

    @Override
    public final Long save(final Tag tag) {
        long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        jdbcTemplate.update(INSERT_TAG, id, tag.getName());
        return id;
    }

    @Override
    public final void delete(final Long id) {
        jdbcTemplate.update(DELETE_TAG_REF, id);
        jdbcTemplate.update(DELETE_TAG, id);
    }
}
