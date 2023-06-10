package com.epam.esm.dto;

import com.epam.esm.entity.Permission;
import com.epam.esm.entity.RoleType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class RoleDto extends RepresentationModel<RoleDto> {
    private Long id;
    private RoleType permission;
    private Set<Permission> authorities;
}
