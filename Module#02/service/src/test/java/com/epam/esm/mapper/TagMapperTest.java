package com.epam.esm.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TagMapperTest {

    @InjectMocks
    private TagMapper mapper = TagMapper.tagMapper;

    @InjectMocks
    private CertificateMapper certificateMapper = CertificateMapper.mapper;

    @Test
    void testToDto() {
        Tag tag = Tag.builder()
                .id(1L)
                .name("Test tag")
                .build();

        TagDto dto = mapper.toDto(tag);

        assertEquals(tag.getId(), dto.getId());
        assertEquals(tag.getName(), dto.getName());
    }

    @Test
    void testToEntity() {
        TagDto dto = TagDto.builder()
                .id(1L)
                .name("Test tag")
                .build();

        Tag tag = mapper.toEntity(dto);

        assertEquals(dto.getId(), tag.getId());
        assertEquals(dto.getName(), tag.getName());
    }

    @Test
    void testToTagSet() {
        TagDto dto1 = TagDto.builder().id(1L).name("Tag1").build();
        TagDto dto2 = TagDto.builder().id(2L).name("Tag2").build();
        Set<TagDto> tagDtos = new HashSet<>(Arrays.asList(dto1, dto2));

        Tag tag1 = Tag.builder().id(1L).name("Tag1").build();
        Tag tag2 = Tag.builder().id(2L).name("Tag2").build();
        Set<Tag> expected = new HashSet<>(Arrays.asList(tag1, tag2));
        Set<Tag> result = certificateMapper.toTagSet(tagDtos);
        assertEquals(expected, result);
    }
}
