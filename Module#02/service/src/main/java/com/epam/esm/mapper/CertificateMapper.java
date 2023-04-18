package com.epam.esm.mapper;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.entity.Certificate;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * This interface maps Certificate and CertificateDto objects to each other using the MapStruct library.
 * <p>
 * It uses the SPRING component model, InjectionStrategy, and TagMapper as a dependency.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {TagMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CertificateMapper {
    /**
     * Maps a CertificateDto object to a Certificate object.
     *
     * @param certificateDto the CertificateDto object to map
     * @return the mapped Certificate object
     */
    @Mapping(target = "tags", source = "tags", qualifiedByName = "toTagSet")
    Certificate toEntity(CertificateDto certificateDto);

    /**
     * Maps a Certificate object to a CertificateDto object.
     *
     * @param certificate the Certificate object to map
     * @return the mapped CertificateDto object
     */
    @Mapping(target = "tags", source = "tags", qualifiedByName = "toTagDtoSet")
    CertificateDto toDto(Certificate certificate);

    /**
     * Maps a list of Certificate objects to a list of CertificateDto objects.
     *
     * @param certificates the list of Certificate objects to map
     * @return the mapped list of CertificateDto objects
     */
    @Mapping(target = "tags", source = "tags", qualifiedByName = "toTagDtoSet")
    List<CertificateDto> toDtoList(List<Certificate> certificates);

    /**
     * Maps a list of CertificateDto objects to a list of Certificate objects.
     *
     * @param certificateDtos the list of CertificateDto objects to map
     * @return the mapped list of Certificate objects
     */
    @Mapping(target = "tags", source = "tags", qualifiedByName = "toTagSet")
    List<Certificate> toListEntity(List<CertificateDto> certificateDtos);

    /**
     * Maps a list of Certificate objects to a list
     * of CertificateWithoutTagDto objects, without tags information.
     *
     * @param certificateList the list of Certificate objects to map
     * @return the mapped list of CertificateWithoutTagDto objects, without tags information
     */
    @Mapping(target = "tags", source = "tags", qualifiedByName = "toTagDtoSet")
    List<CertificateWithoutTagDto> toDtoWithoutTagsList(List<Certificate> certificateList);
}
