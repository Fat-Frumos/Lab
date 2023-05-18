package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CertificateMapper.class})
public interface OrderMapper {

    @Mapping(source = "user", target = "user")
    @Mapping(source = "certificates", target = "certificateDtos")
    OrderDto toDto(Order order);

    @Mapping(source = "user", target = "user")
    Order toEntity(OrderDto orderDto);

    @Mapping(target = "certificateDtos", source = "certificates")
    List<OrderDto> toDtoList(List<Order> orders);

    @Mapping(target = "certificates", source = "certificateDtos")
    List<Order> toEntityList(List<OrderDto> orderDtos);
}
