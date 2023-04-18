package com.epam.esm.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * This is a Spring component that serves as a mapper between Tag and TagDto objects.
 * <p>
 * It uses the MapStruct library to automatically generate the mapping code.
 * <p>
 * The mapper supports converting a Tag object to a TagDto object and vice versa,
 * <p>
 * as well as converting sets of Tag and TagDto objects to each other.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TagMapper {
    /**
     * Maps a Tag object to a TagDto object.
     *
     * @param tag The Tag object to be mapped.
     * @return The resulting TagDto object.
     */
    TagDto toDto(Tag tag);

    /**
     * Maps a TagDto object to a Tag object.
     *
     * @param dto The TagDto object to be mapped.
     * @return The resulting Tag object.
     */
    Tag toEntity(TagDto dto);

    /**
     * Maps a set of TagDto objects to a set of Tag objects.
     *
     * @param tagDtos The set of TagDto objects to be mapped.
     * @return The resulting set of Tag objects.
     */
    @Named("toTagSet")
    default Set<Tag> toTagSet(Set<TagDto> tagDtos) {

        return tagDtos == null ? null
                : tagDtos.stream()
                .map(this::toEntity)
                .collect(toSet());
    }

    /**
     * Maps a set of Tag objects to a set of TagDto objects.
     *
     * @param tags The set of Tag objects to be mapped.
     * @return The resulting set of TagDto objects.
     */
    @Named("toTagDtoSet")
    default Set<TagDto> toTagDtoSet(Set<Tag> tags) {
        return tags == null ? null
                : tags.stream()
                .map(this::toDto)
                .collect(toSet());
    }
}
