package com.epam.esm.mapper;

@org.mapstruct.Mapper(componentModel = "spring")
public interface EntityMapper<E, D> {

    D toDto(E e);

    E toEntity(D d);
}