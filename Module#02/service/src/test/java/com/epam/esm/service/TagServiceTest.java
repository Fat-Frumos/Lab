package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.TagDaoImpl;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagAlreadyExistsException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.mapper.TagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TagServiceTest {
    @Mock
    private final TagDao tagDao = mock(TagDaoImpl.class);
    @Mock
    private TagMapper tagMapper = mock(TagMapper.class);
    @InjectMocks
    private final TagService tagService = new TagServiceImpl(tagDao, tagMapper);
    List<Tag> tags;
    List<TagDto> tagDtos;
    String tagName = "Test";
    TagDto tagDto = TagDto.builder().name(tagName).build();
    Long id = 1L;
    Tag tag = Tag.builder().id(id).name(tagName).build();

    @BeforeEach
    void setup() {

        tags = List.of(
                Tag.builder().id(1L).name("Tag1").build(),
                Tag.builder().id(2L).name("Tag2").build(),
                Tag.builder().id(3L).name("Tag3").build()
        );
        tagDtos = List.of(
                TagDto.builder().id(1L).name("Tag1").build(),
                TagDto.builder().id(2L).name("Tag2").build(),
                TagDto.builder().id(3L).name("Tag3").build()
        );
    }

    @Test
    @DisplayName("Should throw TagAlreadyExistsException when tag with specified name already exists")
    void testSaveTagWhenTagWithNameAlreadyExists() {
        TagDto tagDto = TagDto.builder().id(1L).name("test_tag").build();
        Tag tag = Tag.builder().id(1L).name("test_tag").build();
        when(tagDao.getByName(tagDto.getName())).thenReturn(tag);

        assertThrows(TagAlreadyExistsException.class, () -> tagService.save(tagDto));

        verify(tagDao).getByName(tagDto.getName());
    }

    @Test
    @DisplayName("Should save tag when tag with specified name does not exist")
    void testSaveTagWhenTagWithNameDoesNotExist() {
        TagDto tagDto = TagDto.builder().id(id).name("test_tag").build();
        Tag tag = Tag.builder().id(id).name("test_tag").build();
        when(tagDao.getByName(tagDto.getName())).thenReturn(null);
        when(tagMapper.toEntity(tagDto)).thenReturn(tag);
        when(tagService.getById(id)).thenReturn(tagDto);
        when(tagDao.save(tag)).thenReturn(id);
        TagDto result = tagService.save(tagDto);
        verify(tagDao).getByName(tagDto.getName());
        verify(tagMapper).toEntity(tagDto);
        verify(tagDao).save(tag);
        assertEquals(tagDto, result);
    }

    @ParameterizedTest
    @DisplayName("Should throw TagAlreadyExistsException when saving tag with existing name")
    @MethodSource("existingTagNames")
    void testSaveTagWithExistingName(String tagName) {
        TagDto tagDto = TagDto.builder().name(tagName).build();
        Tag tag = Tag.builder().name(tagName).build();
        when(tagDao.getByName(tagName)).thenReturn(tag);
        when(tagMapper.toEntity(tagDto)).thenReturn(tag);
        assertThrows(TagAlreadyExistsException.class, () -> tagService.save(tagDto));
        verify(tagDao, times(1)).getByName(tagName);
    }

    private static Stream<Arguments> existingTagNames() {
        return Stream.of(
                Arguments.of("Tag1"),
                Arguments.of("Tag2"),
                Arguments.of("Tag3")
        );
    }

    static Stream<String> existingTagNamesProvider() {
        return Stream.of("Test tag1", "Test tag2", "Test tag3");
    }

    @ParameterizedTest
    @MethodSource("existingTagNamesProvider")
    @DisplayName("Should throw TagAlreadyExistsException when tag with specified name already exists")
    void testSaveShouldThrowTagAlreadyExistsException(String tagName) {
        when(tagDao.getByName(tagName)).thenReturn(Tag.builder().name(tagName).build());
        assertThrows(TagAlreadyExistsException.class, () -> tagService.save(TagDto.builder().name(tagName).build()));
        verify(tagDao, times(1)).getByName(tagName);
    }

    @Test
    @DisplayName("Should return all tags")
    void testGetAllShouldReturnAllTags() {
        when(tagDao.getAll()).thenReturn(tags);
        when(tagMapper.toDto(any(Tag.class))).thenReturn(
                new TagDto(tags.get(0).getId(), tags.get(0).getName()),
                new TagDto(tags.get(1).getId(), tags.get(1).getName()),
                new TagDto(tags.get(2).getId(), tags.get(2).getName())
        );
        List<TagDto> actualTagDtos = tagService.getAll();
        assertEquals(tagDtos, actualTagDtos);
    }

    @Test
    @DisplayName("Should return all tags")
    void testGetAllShouldReturnAllTag() {
        when(tagDao.getAll()).thenReturn(tags);
        when(tagMapper.toDto(any())).thenReturn(tagDtos.get(0), tagDtos.get(1), tagDtos.get(2));
        assertEquals(tagDtos, tagService.getAll());
        verify(tagDao, times(1)).getAll();
        verify(tagMapper, times(3)).toDto(any());
    }

    @Test
    @DisplayName("Should return an empty list when there are no tags")
    void testGetAllWhenNoTags() {
        List<Tag> tags = Collections.emptyList();
        List<TagDto> tagDtos = Collections.emptyList();
        when(tagDao.getAll()).thenReturn(tags);

        List<TagDto> result = tagService.getAll();

        assertEquals(tagDtos, result);
        verify(tagDao, times(1)).getAll();
        verify(tagMapper, times(0)).toDto(any());
    }

    @ParameterizedTest
    @DisplayName("Should return empty list when no tags are found")
    @MethodSource("provideEmptyTagLists")
    void testGetAllShouldReturnEmptyList(List<Tag> emptyTagList) {
        when(tagDao.getAll()).thenReturn(emptyTagList);
        List<TagDto> actualTagDtos = tagService.getAll();
        assertTrue(actualTagDtos.isEmpty());
    }

    static Stream<Arguments> provideEmptyTagLists() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(Collections.emptyList())
        );
    }

    @Test
    @DisplayName("Should return an empty list when there are no tags")
    void testGetAllWhenNoTag() {
        List<Tag> tags = Collections.emptyList();
        List<TagDto> tagDtos = Collections.emptyList();
        when(tagDao.getAll()).thenReturn(tags);
        List<TagDto> result = tagService.getAll();

        assertEquals(tagDtos, result);
        verify(tagDao, times(1)).getAll();
        verify(tagMapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Should return tag with specified id")
    void testGetByIdShouldReturnTag() {
        when(tagDao.getById(id)).thenReturn(tag);
        when(tagMapper.toDto(tag)).thenReturn(tagDto);
        TagDto actualTag = tagService.getById(id);
        assertEquals(tagDto, actualTag);
    }

    @Test
    @DisplayName("Should return tag with specified id")
    void testGetByIdShouldReturnTags() {
        long id = 1L;
        when(tagDao.getById(id)).thenReturn(tag);
        when(tagMapper.toDto(tag)).thenReturn(tagDto);
        TagDto expected = tagDto;
        TagDto actual = tagService.getById(id);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"Test, 1", "Gift, 2", "Tag, 3"})
    @DisplayName("Get tag by name")
    void getByName(String name, Long id) {
        TagDto tagDto = TagDto.builder()
                .id(id)
                .name(name)
                .build();
        Tag tag = Tag.builder()
                .id(id)
                .name(name)
                .build();
        when(tagDao.getByName(name)).thenReturn(tag);
        when(tagMapper.toDto(tag)).thenReturn(tagDto);
        assertEquals(tagDto, tagService.getByName(name));
        verify(tagDao, times(1)).getByName(name);
    }

    @Test
    @DisplayName("Delete tag by id")
    void testDelete() {
        when(tagDao.getById(id)).thenReturn(tag);
        tagService.delete(id);
        verify(tagDao).getById(id);
        verify(tagDao).delete(id);
        verify(tagDao, times(1)).delete(id);
    }

    @Test
    @DisplayName("Delete tag by id when not found")
    void deleteNotFound() {
        when(tagDao.getById(id)).thenReturn(null);
        assertThrows(TagNotFoundException.class,
                () -> tagService.delete(id),
                "throw TagNotFoundException");
        verify(tagDao).getById(id);
    }

    @ParameterizedTest
    @CsvSource({"1, Winter", "2, Summer", "3, Spring"})
    @DisplayName("Save tag with existing name should throw exception")
    void saveTagWithNameExist(Long id, String tagName) {
        TagDto tagDto = new TagDto(null, tagName);
        Tag tag = Tag.builder().id(id).name(tagName).build();
        when(tagDao.getByName(tagDto.getName())).thenReturn(tag);
        Exception ex = assertThrows(TagAlreadyExistsException.class, () -> tagService.save(tagDto));
        assertEquals(tagDto.getName(), ex.getMessage());
        verify(tagDao).getByName(tagDto.getName());
        verifyNoMoreInteractions(tagDao);
    }
}
