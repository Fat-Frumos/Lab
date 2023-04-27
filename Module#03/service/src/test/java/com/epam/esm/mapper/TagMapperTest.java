package com.epam.esm.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagMapperTest {
    private TagMapper tagMapper;
    @Mock
    private Tag tag;

    TagDto tagDto1;
    TagDto tagDto2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tagMapper = Mappers.getMapper(TagMapper.class);

        tagDto1 = TagDto.builder()
                .tagId(1L)
                .name("Test Tag 1")
                .build();

        tagDto2 = TagDto.builder()
                .tagId(2L)
                .name("Test Tag 2")
                .build();
    }

    @ParameterizedTest
    @CsvSource({"1, testTag","2, testTag2"})
    void testToDto(Long id, String Name) {
        when(tag.getTagId()).thenReturn(id);
        when(tag.getName()).thenReturn(Name);
        TagDto dto = tagMapper.toDto(tag);
        assertEquals(id, dto.getTagId());
        assertEquals(Name, dto.getName());
    }

    @ParameterizedTest
    @CsvSource({"1, testTag","2, testTag2"})
    void testToEntity(Long id, String Name) {
        TagDto dto = TagDto.builder().tagId(id).name(Name).build();
        Tag entity = tagMapper.toEntity(dto);
        assertEquals(id, entity.getTagId());
        assertEquals(Name, entity.getName());
    }

    @ParameterizedTest
    @CsvSource({"1, testTag","2, testTag2"})
    void testToTagSet(Long id, String Name) {
        TagDto dto = TagDto.builder().tagId(id).name(Name).build();
        Set<TagDto> dtoSet = new HashSet<>(Collections.singletonList(dto));
        Set<Tag> tagSet = tagMapper.toTagSet(dtoSet);
        assertEquals(1, tagSet.size());
        Tag entity = tagSet.iterator().next();
        assertEquals(id, entity.getTagId());
        assertEquals(Name, entity.getName());
    }

    @ParameterizedTest
    @CsvSource({"1, testTag"})
    void testToTagDtoSet(Long id, String Name) {
        Tag entity = Tag.builder().tagId(id).name(Name).build();
        Set<Tag> entitySet = new HashSet<>(Collections.singletonList(entity));
        Set<TagDto> dtoSet = tagMapper.toTagDtoSet(entitySet);
        assertEquals(1, dtoSet.size());
        TagDto dto = dtoSet.iterator().next();
        assertEquals(id, dto.getTagId());
        assertEquals(Name, dto.getName());
    }


    @Test
    void testToEntity() {
        TagDto dto = TagDto.builder()
                .tagId(1L)
                .name("Test tag")
                .build();
        Tag tag = tagMapper.toEntity(dto);
        assertEquals(dto.getTagId(), tag.getTagId());
        assertEquals(dto.getName(), tag.getName());
    }

    @Test
    void shouldMapSetOfTagDtoToSetOfTag() {

        Set<TagDto> tagDtoSet = new HashSet<>();
        tagDtoSet.add(tagDto1);
        tagDtoSet.add(tagDto2);
        Set<Tag> tagSet = tagMapper.toTagSet(tagDtoSet);

        assertEquals(tagDtoSet.size(), tagSet.size());

        assertTrue(tagSet.stream()
                .anyMatch(tag -> tag.getTagId().equals(tagDto1.getTagId())
                        && tag.getName().equals(tagDto1.getName())));

        assertEquals(true, tagSet.stream()
                .anyMatch(tag -> tag.getTagId().equals(tagDto2.getTagId())
                        && tag.getName().equals(tagDto2.getName())));
    }

    @Test
    void testToTagSet() {
        Set<TagDto> tagDtos = new HashSet<>(Arrays.asList(tagDto1, tagDto2));
        Tag tag1 = Tag.builder().tagId(1L).name("Test Tag 1").build();
        Tag tag2 = Tag.builder().tagId(2L).name("Test Tag 2").build();
        Set<Tag> expected = new HashSet<>(Arrays.asList(tag1, tag2));
        Set<Tag> result = tagMapper.toTagSet(tagDtos);
        assertEquals(expected, result);
    }
}
