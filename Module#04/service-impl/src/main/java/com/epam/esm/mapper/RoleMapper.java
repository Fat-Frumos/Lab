package com.epam.esm.mapper;

import com.epam.esm.dto.RoleDto;
import com.epam.esm.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    /**
     * Converts a RoleDto object to a Role entity.
     *
     * @param roleDto The RoleDto object to convert.
     * @return The corresponding Role entity.
     */
    @Named("toRole")
    Role toEntity(RoleDto roleDto);

    /**
     * Converts a Role entity to a RoleDto object.
     *
     * @param role The Role entity to convert.
     * @return The corresponding RoleDto object.
     */
    @Named("toRoleDto")
    RoleDto toDto(Role role);
}
