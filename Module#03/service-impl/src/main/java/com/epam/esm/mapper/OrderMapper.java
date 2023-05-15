package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

//    @Mapping(target = "certificates", source = "certificates", qualifiedByName = "toCertificateDtoList")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class, CertificateMapper.class})
public interface OrderMapper {
    @Mapping(source = "user", target = "user")
    OrderDto toDto(Order order);

    @Mapping(source = "user", target = "user")
    Order toEntity(OrderDto dto);


    @Named("toOrderSet")
    default Set<Order> toOrderSet(Set<OrderDto> orderDtos) {
        return orderDtos.stream()
                .map(this::toEntity)
                .collect(toSet());
    }
}
