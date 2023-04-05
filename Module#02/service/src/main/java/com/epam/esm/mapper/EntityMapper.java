package com.epam.esm.mapper;

import com.epam.esm.dto.BaseDto;

@org.mapstruct.Mapper(componentModel = "spring")
public interface EntityMapper<E, D extends BaseDto> {

    D toDto(E e);

    E toEntity(D d);
}