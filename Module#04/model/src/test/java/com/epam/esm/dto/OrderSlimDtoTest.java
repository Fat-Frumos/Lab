package com.epam.esm.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderSlimDtoTest {
    @Test
    void testOrderSlimDto() {
        UserSlimDto user = UserSlimDto.builder()
                .id(1L)
                .username("user")
                .build();

        OrderSlimDto order = OrderSlimDto.builder()
                .id(1L)
                .user(user)
                .cost(new BigDecimal("99.99"))
                .orderDate(Timestamp.valueOf("2023-07-29 00:12:38"))
                .build();

        assertEquals(1L, order.getId());
        assertEquals(user, order.getUser());
        assertEquals(new BigDecimal("99.99"), order.getCost());
        assertEquals(Timestamp.valueOf("2023-07-29 00:12:38"), order.getOrderDate());
    }
}
