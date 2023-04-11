package com.epam.esm.mapper;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

import static com.epam.esm.mapper.TagMapper.tagMapper;
import static java.util.stream.Collectors.toSet;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {TagMapper.class})
public interface CertificateMapper {

    CertificateMapper mapper = Mappers.getMapper(CertificateMapper.class);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "toTagSet")
    Certificate toEntity(CertificateDto certificateDto);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "toTagDtoSet")
    CertificateDto toDto(Certificate certificate);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "toTagDtoSet")
    List<CertificateDto> toDtoList(List<Certificate> certificates);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "toTagSet")
    List<Certificate> toListEntity(List<CertificateDto> certificateDtos);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "toTagDtoSet")
    List<CertificateWithoutTagDto> toDtoWithoutTagsList(List<Certificate> certificateList);

    @Named("toTagSet")
    default Set<Tag> toTagSet(Set<TagDto> tagDtos) {
        return tagDtos == null ? null
                : tagDtos.stream()
                .map(tagMapper::toEntity)
                .collect(toSet());
    }

    @Named("toTagDtoSet")
    default Set<TagDto> toTagDtoSet(Set<Tag> tags) {
        return tags == null ? null
                : tags.stream()
                .map(tagMapper::toDto)
                .collect(toSet());
    }
}
