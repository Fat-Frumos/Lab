package com.epam.esm.mapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING) //, uses = {CertificateMapper.class}
public interface UserMapper {

    //    @Mapping(target = "certificates", source = "certificates", qualifiedByName = "toCertificateDtoList")
    UserDto toDto(User user);

    User toEntity(UserDto dto);

//    List<UserDto> toDtoList(List<User> users);
}
