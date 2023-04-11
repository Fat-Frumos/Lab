package com.epam.esm.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {

    TagMapper tagMapper = Mappers.getMapper(TagMapper.class);

    TagDto toDto(Tag tag);

    Tag toEntity(TagDto dto);
}
