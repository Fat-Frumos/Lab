package com.epam.esm.dao;

import com.epam.esm.mapper.TagRowMapper;
import com.epam.esm.domain.Tag;
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
    public final Tag getById(
            final Long id)
            throws RuntimeException {
        return jdbcTemplate.queryForObject(
                GET_TAG_BY_ID,
                new Object[]{id},
                tagRowMapper);

    }

    @Override
    public final Tag getByName(
            final String name)
            throws RuntimeException {
        return jdbcTemplate.queryForObject(
                GET_BY_NAME,
                new Object[]{name},
                tagRowMapper);
    }

    @Override
    public final List<Tag> getAll()
            throws RuntimeException {
        return jdbcTemplate.query(
                GET_ALL_TAGS,
                tagRowMapper);
    }

    @Override
    public final boolean save(
            final Tag tag)
            throws RuntimeException {
        return false; //TODO
    }

    @Override
    public final boolean delete(
            final Long id)
            throws RuntimeException {
        return jdbcTemplate.update(
                DELETE_TAG, id) > 0;
    }
}
