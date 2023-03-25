package com.epam.esm.dao;

import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.DaoException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.epam.esm.dao.mapper.QueriesContext.*;

@Repository
@RequiredArgsConstructor
public class DefaultTagDao implements TagDao {

    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper tagRowMapper;

    private static final Logger LOGGER = LogManager.getLogger();
    @Override
    public final Tag getById(final Long id)
            throws DaoException {
        try {
            return jdbcTemplate.queryForObject(
                    GET_TAG_BY_ID,
                    new Object[]{id},
                    tagRowMapper);
        } catch (Exception e) {
            LOGGER.debug(GET_TAG_BY_ID);
            throw new DaoException(e);
        }
    }

    @Override
    public final Tag getByName(final String name)
            throws DaoException {
        try {
            return jdbcTemplate.queryForObject(
                    GET_BY_NAME,
                    new Object[]{name},
                    tagRowMapper);
        } catch (Exception e) {
            LOGGER.debug(GET_TAG_BY_ID);
            throw new DaoException(e);
        }
    }

    @Override
    public final List<Tag> getAll()
            throws DaoException {
        try {
            return jdbcTemplate.query(
                    GET_ALL_TAGS,
                    tagRowMapper);
        } catch (Exception e) {
            LOGGER.debug(GET_ALL_TAGS);
            throw new DaoException(e);
        }
    }

    @Override
    public final boolean save(final Tag tag)
            throws DaoException {
        try { // TODO
            return false;
        } catch (Exception e) {
            LOGGER.debug(INSERT_TAG);
            throw new DaoException(e);
        }
    }

    @Override
    public final boolean delete(final Long id)
            throws DaoException {
        try {
            return jdbcTemplate.update(
                    DELETE_TAG, id) > 0;
        } catch (Exception e) {
            LOGGER.debug(GET_TAG_BY_ID);
            throw new DaoException(e);
        }
    }
}
