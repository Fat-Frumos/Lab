package com.epam.esm.mapper;

import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TagMapper implements EntityMapper<Tag, TagDto> {
    public static final TagMapper mapper = new TagMapper();

    public static TagMapper getInstance() {
        return mapper;
    }

    private TagMapper() {
    }

    @Override
    public TagDto toDto(final Tag tag) {
        if (tag == null) {
            throw new NullPointerException("Tag must not be null");
        }
        return TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    @Override
    public Tag toEntity(TagDto tagDto) {
        if (tagDto == null) { // TODO Tests
            throw new NullPointerException("Tag must not be null");
        }
        return Tag.builder()
                .id(tagDto.getId())
                .name(tagDto.getName())
                .build();
    }
}
