package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.epam.esm.mapper.QueriesContext.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("DefaultTagDao Unit Tests")
@ExtendWith(MockitoExtension.class)
class TestTagDao {

    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private TagRowMapper tagRowMapper;
    private TagDao tagDao;
    private final static String name = "winter";
    Long id = 1L;

    @BeforeEach
    void setup() {
        tagDao = new TagDaoImpl(jdbcTemplate, tagRowMapper);
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
        assertEquals(expectedTag, tagDao.getById(id).orElseThrow());
    }

    @Test
    @DisplayName("Test getByName() with valid name")
    void testGetByNameValid() {
        Tag expectedTag = Tag.builder().id(1L).name(name).build();
        String sql = String.format("%s'%s'", GET_BY_TAG_NAME, name);
        List<Tag> tags = Collections.singletonList(expectedTag);
        when(jdbcTemplate.query(sql, tagRowMapper)).thenReturn(tags);
        Optional<Tag> optionalTag = tagDao.getByName(name);
        if (optionalTag.isPresent()) {
            assertNotNull(optionalTag);
            assertEquals(expectedTag, optionalTag.get());
        }
        verify(jdbcTemplate).query(sql, tagRowMapper);
    }

    @Test
    @DisplayName("Test getByName() with winter name")
    void testGetByNameUnknown() {
        String sql = String.format("%s'%s'", GET_BY_TAG_NAME, name);
        given(jdbcTemplate.query(sql, tagRowMapper)).willReturn(Collections.emptyList());
        assertTrue(tagDao.getByName(name).isEmpty());
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
        Optional<Tag> tag = tagDao.getByName(name);
        if (tag.isPresent()) {
            assertEquals(expectedId, tag.get().getId());
            assertEquals(name, tag.get().getName());
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
        when(jdbcTemplate.update(eq(INSERT_TAG), anyLong(), eq(tag.getName()))).thenReturn(expectedResult ? 1 : 0);
        assertTrue(tagDao.save(tag) > 0);
    }

    private static Stream<Arguments> tagProvider() {
        return Stream.of(
                Arguments.of(Tag.builder().name("tag1").build(), true),
                Arguments.of(Tag.builder().name(null).build(), false),
                Arguments.of(Tag.builder().name("").build(), false)
        );
    }

    @Test
    @DisplayName("Should throw exception if tag with specified id not found")
    void testGetByIdShouldThrowExceptionIfTagNotFound() {
        when(jdbcTemplate.queryForObject(GET_TAG_BY_ID, new Object[]{id}, tagRowMapper))
                .thenThrow(TagNotFoundException.class);
        assertThrows(TagNotFoundException.class, () -> tagDao.getById(id));
    }

    @ParameterizedTest
    @DisplayName("Should return tag when tag exists")
    @CsvSource({"1,test_tag"})
    void getByIdShouldReturnTagWhenTagExists(long id, String tagName) {
        Tag expectedTag = Tag.builder().id(id).name(tagName).build();
        when(jdbcTemplate.queryForObject(eq(GET_TAG_BY_ID), any(Object[].class), eq(tagRowMapper)))
                .thenReturn(expectedTag);
        Optional<Tag> actualTag = tagDao.getById(id);

        assertTrue(actualTag.isPresent());
        assertEquals(expectedTag, actualTag.get());
        verify(jdbcTemplate).queryForObject(eq(GET_TAG_BY_ID), any(Object[].class), eq(tagRowMapper));
    }

    @ParameterizedTest
    @DisplayName("Should return empty optional when tag does not exist")
    @CsvSource({"1"})
    void getByIdShouldReturnEmptyOptionalWhenTagDoesNotExist(long id) {
        when(jdbcTemplate.queryForObject(eq(GET_TAG_BY_ID), any(Object[].class), eq(tagRowMapper)))
                .thenReturn(null);
        Optional<Tag> actualTag = tagDao.getById(id);

        assertFalse(actualTag.isPresent());
        verify(jdbcTemplate).queryForObject(eq(GET_TAG_BY_ID), any(Object[].class), eq(tagRowMapper));
    }

    @ParameterizedTest
    @DisplayName("Given existing tag ID, when getById() called, then return Optional containing tag")
    @CsvSource({"1,Test Tag"})
    void getById_givenExistingTagId_thenReturnsOptionalContainingTag(Long tagId, String tagName) {
        Tag expectedTag = Tag.builder().id(tagId).name(tagName).build();
        Object[] args = new Object[]{tagId};
        when(jdbcTemplate.queryForObject(GET_TAG_BY_ID, args, tagRowMapper)).thenReturn(expectedTag);
        Optional<Tag> result = tagDao.getById(tagId);
        assertTrue(result.isPresent());
        assertEquals(expectedTag, result.get());
    }

    @ParameterizedTest
    @DisplayName("Given non-existing tag ID, when getById() called, then return empty Optional")
    @CsvSource({"100", "999"})
    void getById_givenNonExistingTagId_thenReturnsEmptyOptional(Long tagId) {
        Object[] args = new Object[]{tagId};
        when(jdbcTemplate.queryForObject(GET_TAG_BY_ID, args, tagRowMapper)).thenReturn(null);
        Optional<Tag> result = tagDao.getById(tagId);
        assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @DisplayName("Should delete tag with given id")
    @CsvSource({"1", "2", "3"})
    void deleteShouldRemoveTagWithGivenId(Long id) {
        tagDao.delete(id);
        verify(jdbcTemplate).update(DELETE_TAG_REF, id);
        verify(jdbcTemplate).update(DELETE_TAG, id);
    }
}
