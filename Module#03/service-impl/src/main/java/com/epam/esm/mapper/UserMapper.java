package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

/**
 * This interface defines mapping methods between User and UserDto objects.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Converts a UserDto object to a User entity.
     *
     * @param userDto The UserDto object to convert.
     * @return The corresponding User entity.
     */
    @Mapping(target = "orders", source = "orderDtos",
            qualifiedByName = "toOrderSet")
    User toEntity(UserDto userDto);

    /**
     * Converts a User entity to a UserDto object.
     *
     * @param user The User entity to convert.
     * @return The corresponding UserDto object.
     */
    @Mapping(target = "orderDtos", source = "orders",
            qualifiedByName = "toOrderDtoSet")
    UserDto toDto(User user);

    /**
     * Converts a list of User entities to a list of UserDto objects.
     *
     * @param users The list of User entities to convert.
     * @return A list of corresponding UserDto objects.
     */
    @Mapping(target = "orderDtos", source = "orders")
    List<UserDto> toDtoList(List<User> users);

    /**
     * Converts a set of OrderDto objects to a set of Order entities.
     *
     * @param orderDtos The set of OrderDto objects to convert.
     * @return A set of corresponding Order entities.
     */
    @Named("toOrderSet")
    Set<Order> toOrderSet(Set<OrderDto> orderDtos);

    /**
     * Converts a set of Order entities to a set of OrderDto objects.
     *
     * @param orders The set of Order entities to convert.
     * @return A set of corresponding OrderDto objects.
     */
    @Named("toOrderDtoSet")
    Set<OrderDto> toOrderDtoSet(Set<Order> orders);
}
