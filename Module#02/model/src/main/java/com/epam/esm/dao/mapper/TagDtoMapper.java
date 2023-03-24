package com.epam.esm.dao.mapper;

import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
public class TagDtoMapper {
    public TagDto toDto(final Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }
}
