package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "orders", source = "orderDtos", qualifiedByName = "toOrderSet")
    User toEntity(UserDto userDto);

    @Mapping(target = "orderDtos", source = "orders", qualifiedByName = "toOrderDtoSet")
    UserDto toDto(User user);

    @Mapping(target = "orderDtos", source = "orders")
    List<UserDto> toDtoList(List<User> user);

    @Named("toOrderSet")
    Set<Order> toOrderSet(Set<OrderDto> orderDtos);

    @Named("toOrderDtoSet")
    Set<OrderDto> toOrderDtoSet(Set<Order> orders);


}