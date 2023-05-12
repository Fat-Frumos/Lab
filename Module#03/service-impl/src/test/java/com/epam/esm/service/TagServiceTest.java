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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        tags = new ArrayList<>();
        tags.add(Tag.builder().id(1L).name("Tag1").build());
        tags.add(Tag.builder().id(2L).name("Tag2").build());
        tags.add(Tag.builder().id(3L).name("Tag3").build());
        tagDtos = new ArrayList<>();
        tagDtos.add(TagDto.builder().id(1L).name("Tag1").build());
        tagDtos.add(TagDto.builder().id(2L).name("Tag2").build());
        tagDtos.add(TagDto.builder().id(3L).name("Tag3").build());
    }

    @Test
    @DisplayName("Should throw TagAlreadyExistsException when tag with specified name already exists")
    void testSaveTagWhenTagWithNameAlreadyExists() {
        TagDto tagDto = TagDto.builder().id(1L).name("test_tag").build();
        Tag tag = Tag.builder().id(1L).name("test_tag").build();
        when(tagDao.getByName(tagDto.getName())).thenReturn(Optional.of(tag));

        assertThrows(TagAlreadyExistsException.class, () -> tagService.save(tagDto));

        verify(tagDao).getByName(tagDto.getName());
    }


    @ParameterizedTest
    @DisplayName("Should throw TagAlreadyExistsException when saving tag with existing name")
    @MethodSource("existingTagNames")
    void testSaveTagWithExistingName(String tagName) {
        TagDto tagDto = TagDto.builder().name(tagName).build();
        Tag tag = Tag.builder().name(tagName).build();
        when(tagDao.getByName(tagName)).thenReturn(Optional.of(tag));
        when(tagMapper.toEntity(tagDto)).thenReturn(tag);
        assertThrows(TagAlreadyExistsException.class, () -> tagService.save(tagDto));
        verify(tagDao, times(1)).getByName(tagName);
    }

    private static Stream<Arguments> existingTagNames() {
        return Stream.of(
                Arguments.of("Winter"),
                Arguments.of("Spring"),
                Arguments.of("Summer"),
                Arguments.of("Autumn")
        );
    }

    static Stream<String> existingTagNamesProvider() {
        return Stream.of("Test tag1", "Test tag2", "Test tag3");
    }

    @ParameterizedTest
    @MethodSource("existingTagNamesProvider")
    @DisplayName("Should throw TagAlreadyExistsException when tag with specified name already exists")
    void testSaveShouldThrowTagAlreadyExistsException(String tagName) {
        when(tagDao.getByName(tagName)).thenReturn(Optional.of(Tag.builder().name(tagName).build()));
        assertThrows(TagAlreadyExistsException.class, () -> tagService.save(TagDto.builder().name(tagName).build()));
        verify(tagDao, times(1)).getByName(tagName);
    }

    @Test
    @DisplayName("Should return all tags")
    void testGetAllShouldReturnAllTags() {
        when(tagDao.getAll()).thenReturn(tags);
        when(tagMapper.toDto(any(Tag.class))).thenReturn(
                TagDto.builder().id(tags.get(0).getId()).name(tags.get(0).getName()).build(),
                TagDto.builder().id(tags.get(1).getId()).name(tags.get(1).getName()).build(),
                TagDto.builder().id(tags.get(2).getId()).name(tags.get(2).getName()).build()
        );
        List<TagDto> actualTagDtos = tagService.getAll();
        IntStream.range(0, actualTagDtos.size()).forEach(i -> assertEquals(tagDtos.get(i), actualTagDtos.get(i)));
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
        when(tagDao.getById(id)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(tagDto);
        TagDto actualTag = tagService.getById(id);
        assertEquals(tagDto, actualTag);
    }

    @Test
    @DisplayName("Should return tag with specified id")
    void testGetByIdShouldReturnTags() {
        when(tagDao.getById(id)).thenReturn(Optional.of(tag));
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
        when(tagDao.getByName(name)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(tagDto);
        assertEquals(tagDto, tagService.getByName(name));
        verify(tagDao, times(1)).getByName(name);
    }

    @Test
    @DisplayName("Delete tag by id")
    void testDelete() {
        when(tagDao.getById(id)).thenReturn(Optional.of(tag));
        tagService.delete(id);
        verify(tagDao).getById(id);
        verify(tagDao).delete(id);
        verify(tagDao, times(1)).delete(id);
    }

    @ParameterizedTest
    @CsvSource({"1, Winter", "2, Summer", "3, Spring"})
    @DisplayName("Save tag with existing name should throw exception")
    void saveTagWithNameExist(Long id, String tagName) {
        TagDto tagDto = TagDto.builder().id(null).name(tagName).build();
        Tag tag = Tag.builder().id(id).name(tagName).build();
        when(tagDao.getByName(tagDto.getName())).thenReturn(Optional.of(tag));
        Exception ex = assertThrows(TagAlreadyExistsException.class, () -> tagService.save(tagDto));
        assertEquals(tagDto.getName(), ex.getMessage());
        verify(tagDao).getByName(tagDto.getName());
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    @DisplayName("test getById returns TagDto when tag exists")
    void testGetByIdReturnsTagDtoWhenTagExists() {
        Tag tag = Tag.builder().id(id).name("test_tag").build();
        TagDto expectedTagDto = TagDto.builder().id(id).name("test_tag").build();
        when(tagDao.getById(id)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(expectedTagDto);
        TagDto actualTagDto = tagService.getById(id);
        assertNotNull(actualTagDto);
        assertEquals(expectedTagDto, actualTagDto);
        verify(tagDao).getById(id);
        verify(tagMapper).toDto(tag);
    }

    @Test
    void testGetById_throwsTagNotFoundException_whenTagDoesNotExist() {
        when(tagDao.getById(id)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.getById(id));
        verify(tagDao).getById(id);
    }

    @ParameterizedTest
    @CsvSource({
            "1, tag1",
            "2, tag2",
            "3, tag3"
    })
    @DisplayName("Should throw TagAlreadyExistsException if tag with the same name already exists")
    void shouldThrowTagAlreadyExistsException(long id, String name) {
        TagDto tagDto = TagDto.builder().name(name).build();
        Tag tag = Tag.builder().id(id).name(name).build();
        when(tagDao.getByName(tagDto.getName())).thenReturn(Optional.of(tag));
        assertThrows(TagAlreadyExistsException.class, () -> tagService.save(tagDto));
        verify(tagDao).getByName(name);
    }

    @DisplayName("getByName() method should return the tag with the given name")
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})

    @ParameterizedTest
    void getByNameShouldReturnTag(final Long tagId, final String name) {
        final Tag tag = Tag.builder().id(tagId).name(name).build();
        final TagDto tagDto = TagDto.builder().id(tagId).name(name).build();

        when(tagDao.getByName(name)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(tagDto);

        final TagDto result = tagService.getByName(name);

        assertNotNull(result);
        assertEquals(tagDto.getId(), result.getId());
        assertEquals(tagDto.getName(), result.getName());

        verify(tagDao).getByName(name);
        verify(tagMapper).toDto(tag);
    }

    @DisplayName("getByName() method should throw TagNotFoundException if tag with the given name not found")
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    @ParameterizedTest
    void getByNameShouldThrowTagNotFoundException(final long id, final String name) {
        when(tagDao.getByName(name)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.getByName(name));
        verify(tagDao).getByName(name);

        when(tagDao.getById(id)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.getById(id));
        verify(tagDao).getById(id);
    }
}
