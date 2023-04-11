package com.epam.esm.service;

import com.epam.esm.dao.DefaultTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.mapper.TagMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultTagServiceTest {
    @Mock
    private final TagDao dao = mock(DefaultTagDao.class);
    @Mock
    private TagMapper tagMapper = mock(TagMapper.class);
    @InjectMocks
    private final TagService service = new DefaultTagService(dao);
    Long tagId = 1L;
    List<TagDto> tagDtos;
    Tag tag = Tag.builder().id(tagId).name("Tag").build();
    List<Tag> tags = new ArrayList<>();

    @BeforeEach
    void setUp() {
        tags.add(Tag.builder().id(1L).name("Tag1").build());
        tags.add(Tag.builder().id(2L).name("Tag2").build());
        tags.add(Tag.builder().id(3L).name("Tag3").build());
        tagDtos = tags.stream().map(tagMapper::toDto).collect(toList());
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
    @DisplayName("Test getAll")
    void testGetAll() {
        when(dao.getAll()).thenReturn(tags);
        List<TagDto> actual = service.getAll();
        assertEquals(tagDtos.size(), actual.size());
        actual.stream().map(TagDto::getName).forEach(Assertions::assertNotNull);
        actual.stream().map(TagDto::getId).forEach(Assertions::assertNotNull);
        verify(dao, times(1)).getAll();
    }

    @Test
    @DisplayName("Test Tag Deletion")
    void testDeleteTag() {
        when(dao.getById(tagId)).thenReturn(tag);
        when(dao.delete(tagId)).thenReturn(true);
        assertTrue(service.delete(tagId));
        verify(dao).delete(tagId);
        verify(dao, times(1)).delete(tag.getId());
    }

    @Test
    @DisplayName("Test Tag Deletion with Invalid Id")
    void testDeleteTagWithInvalidIds() {
        when(dao.getById(tagId)).thenReturn(tag);
        when(dao.delete(tagId)).thenReturn(false);
        assertFalse(service.delete(tagId));
        verify(dao).delete(tagId);
    }

    @Test
    @DisplayName("Test Non existent Tag")
    void testDeleteNonexistentTag() {
        assertThrows(TagNotFoundException.class, () -> service.delete(tagId));
    }
}
