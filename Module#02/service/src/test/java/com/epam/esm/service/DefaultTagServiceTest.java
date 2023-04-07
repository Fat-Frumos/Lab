package com.epam.esm.service;

import com.epam.esm.dao.DefaultTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.mapper.TagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultTagServiceTest {

    @Mock
    private final TagMapper mapper = TagMapper.getInstance();
    private final TagDao dao = mock(DefaultTagDao.class);
    @InjectMocks
    private DefaultTagService service = new DefaultTagService(dao, mapper);
    Long tagId = 1L;
    List<TagDto> tagDtos;
    Tag tag = Tag.builder().id(tagId).name("Tag").build();
    TagDto tagDto = TagDto.builder().id(tagId).name("Tag").build();
    List<Tag> tags = new ArrayList<>();

    @BeforeEach
    void setUp() {
        tags.add(Tag.builder().id(1L).name("Tag1").build());
        tags.add(Tag.builder().id(2L).name("Tag2").build());
        tags.add(Tag.builder().id(3L).name("Tag3").build());
        tagDtos = tags.stream().map(mapper::toDto).collect(toList());
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test getById with invalid id and throws Runtime Exception")
    void testGetByIdWithInvalidIdAssertThrows() {
        assertThrows(RuntimeException.class, () -> service.getById(-1L));
    }

    @Test
    @DisplayName("Test getById and verify invoke")
    void testGetById() {
        when(dao.getById(tagId)).thenReturn(tag);
        TagDto tagDto = service.getById(tagId);
        assertNotNull(tagDto);
        assertEquals(tagId, tagDto.getId());
        assertEquals(tag.getName(), tagDto.getName());
        verify(dao, times(1)).getById(tagId);
    }

    @Test
    @DisplayName("Test getById with invalid id")
    void testGetByIdWithInvalidId() {
        Long invalidTagId = -1L;
        when(dao.getById(invalidTagId)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> service.getById(invalidTagId));
        verify(dao, times(1)).getById(invalidTagId);
    }

    @Test
    @DisplayName("Test getByName and verify invoke")
    void testGetByName() {
        when(dao.getByName(tag.getName())).thenReturn(tag);
        TagDto tagDto = service.getByName(tag.getName());
        assertNotNull(tagDto);
        assertEquals(tagId, tagDto.getId());
        assertEquals(tag.getName(), tagDto.getName());
        verify(dao, times(1)).getByName(tag.getName());
    }

    @Test
    @DisplayName("Test getByName with invalid name and throw RuntimeException")
    void testGetByNameWithInvalidName() {
        when(dao.getByName(tag.getName())).thenReturn(null);
        assertThrows(RuntimeException.class, () -> service.getByName(tag.getName()));
        verify(dao, times(1)).getByName(tag.getName());
    }

    @Test
    @DisplayName("Test getAll")
    void testGetAll() {
        when(dao.getAll()).thenReturn(tags);
        List<TagDto> actual = service.getAll();
        assertEquals(tagDtos.size(), actual.size());
        IntStream.range(0, tagDtos.size()).forEach(i -> assertEquals(tagDtos.get(i), actual.get(i)));
        verify(dao, times(1)).getAll();
    }
    @Test
    @DisplayName("Test save tag")
    void testSaveTag() {
        when(mapper.toEntity(tagDto)).thenReturn(tag);
        when(dao.save(tag)).thenReturn(true);
        assertTrue(service.save(tagDto));
        verify(dao, times(1)).save(tag);
    }

    @Test
    @DisplayName("Test save tag fails")
    void testSaveTagFails() {
        when(mapper.toEntity(tagDto)).thenReturn(tag);
        when(dao.save(tag)).thenReturn(false);
        assertFalse(service.save(tagDto));
        verify(dao, times(1)).save(tag);
    }
    
    @Test
    @DisplayName("Test Tag Deletion")
    void testDeleteTag() {
        when(dao.delete(tagId)).thenReturn(true);
        assertTrue(service.delete(tagId));
        verify(dao).delete(tagId);
        verify(dao, times(1)).delete(tag.getId());
    }

    @Test
    @DisplayName("Test Tag Deletion with Invalid Id")
    void testDeleteTagWithInvalidId() {
        when(dao.delete(tagId)).thenReturn(false);
        assertFalse(service.delete(tagId));
        verify(dao).delete(tagId);
        verify(dao, times(1)).delete(tag.getId());
    }
}
