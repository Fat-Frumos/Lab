package com.epam.esm.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TagMapper {
        TagDto toDto(Tag tag);

        Tag toEntity(TagDto dto);

        @Named("toTagSet")
    default Set<Tag> toTagSet(Set<TagDto> tagDtos) {
        return tagDtos == null ? null
                : tagDtos.stream()
                .map(this::toEntity)
                .collect(toSet());
    }

        @Named("toTagDtoSet")
    default Set<TagDto> toTagDtoSet(Set<Tag> tags) {
        return tags == null ? null
                : tags.stream()
                .map(this::toDto)
                .collect(toSet());
    }
}
