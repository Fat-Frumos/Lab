package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.TagRowMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.epam.esm.mapper.QueriesContext.GET_BY_TAG_NAME;
import static com.epam.esm.mapper.QueriesContext.INSERT_TAG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("DefaultTagDao Unit Tests")
@ExtendWith(MockitoExtension.class)
class DefaultTagDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private TagRowMapper tagRowMapper;
    private DefaultTagDao tagDao;
    private final static String name = "winter";

    @BeforeEach
    void setup() {
        tagDao = new DefaultTagDao(jdbcTemplate, tagRowMapper);
    }

    @DisplayName("Test getById method with different inputs")
    @ParameterizedTest(name = "{index} => id=''{0}'', expected=''{1}''")
    @CsvSource({
            "1,1,tag1",
            "2,2,tag2",
            "3,3,tag3"
    })
    void testGetById(Long id, Long expectedId, String expectedName) {
        Tag expectedTag = Tag.builder().id(expectedId).name(expectedName).build();
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(tagRowMapper))).thenReturn(expectedTag);
        assertEquals(expectedTag, tagDao.getById(id));
    }

    @Test
    @DisplayName("Test getByName() with valid name")
    void testGetByNameValid() {
        Tag expectedTag = Tag.builder().id(1L).name(name).build();
        String sql = String.format("%s'%s'", GET_BY_TAG_NAME, name);
        List<Tag> tags = Collections.singletonList(expectedTag);
        when(jdbcTemplate.query(sql, tagRowMapper)).thenReturn(tags);

        Tag tag = tagDao.getByName(name);
        assertNotNull(tag);
        assertEquals(expectedTag, tag);
        verify(jdbcTemplate).query(sql, tagRowMapper);
    }

    @Test
    @DisplayName("Test getByName() with winter name")
    void testGetByNameUnknown() {
        String sql = String.format("%s'%s'", GET_BY_TAG_NAME, name);
        given(jdbcTemplate.query(sql, tagRowMapper)).willReturn(Collections.emptyList());
        assertNull(tagDao.getByName(name));
    }

    @ParameterizedTest(name = "{index} - name: {0}, expected: {1}")
    @CsvSource({
            "tag1, 1",
            "tag2, 2",
            "tag3, 3",
            "non-existent-tag,"
    })
    void testGetByName(String name, Long expectedId) {
        List<Tag> tags = new ArrayList<>();
        if (expectedId != null) {
            tags.add(Tag.builder().id(expectedId).name(name).build());
        }
        String sql = String.format("%s'%s'", GET_BY_TAG_NAME, name);
        when(jdbcTemplate.query(sql, tagRowMapper)).thenReturn(tags);
        Tag tag = tagDao.getByName(name);

        if (expectedId == null) {
            assertNull(tag);
        } else {
            assertEquals(expectedId, tag.getId());
            assertEquals(name, tag.getName());
        }
        verify(jdbcTemplate).query(sql, tagRowMapper);
    }

    @Test
    @DisplayName("Should return all tags")
    void testGetAll() {
        List<Tag> tags = Arrays.asList(
                Tag.builder().id(1L).name("tag1").build(),
                Tag.builder().id(2L).name("tag2").build()
        );
        String sql = "SELECT t.id tag_id, t.name tag_name FROM tag as t";
        when(jdbcTemplate.query(sql, tagRowMapper)).thenReturn(tags);
        List<Tag> tagList = tagDao.getAll();

        tagList.forEach(Assertions::assertNotNull);
        assertEquals(tags, tagList);
        verify(jdbcTemplate).query(sql, tagRowMapper);
    }

    @ParameterizedTest
    @MethodSource("tagProvider")
    void testSave(Tag tag, boolean expectedResult) {
        when(jdbcTemplate.update(INSERT_TAG, tag.getName())).thenReturn(expectedResult ? 1 : 0);
        assertEquals(expectedResult, tagDao.save(tag));
    }
    private static Collection<Arguments> tagProvider() {
        return Arrays.asList(
                Arguments.of(Tag.builder().name("tag1").build(), true),
                Arguments.of(Tag.builder().name(null).build(), false),
                Arguments.of(Tag.builder().name("").build(), false)
        );
    }
}
