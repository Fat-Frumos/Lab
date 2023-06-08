package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.security.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    String admin = "ROLE_ADMIN";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;
    long id = 1L;
    String tagName = "Summer";
    OrderDto orderDto = OrderDto.builder().id(id).build();
    Tag tag = Tag.builder().id(id).name(tagName).build();
    Page<OrderDto> page = new PageImpl<>(Collections.singletonList(orderDto));

    @Test
    @DisplayName("Get most used tag: Given valid user ID, when get most used tag, then return the most used tag")
    void testGetMostUsedTagShouldReturnMostUsedTagWhenItExists() throws Exception {
        when(orderService.getMostUsedTags(id)).thenReturn(Optional.of(tag));
        mockMvc.perform(get("/orders/users/1/most")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Summer"));
    }

//    @Test
//    @DisplayName("Create order: Given valid order details, when create order, then return HTTP status 201")
//    void testCreateOrderShouldReturnHttpStatusCreated() throws Exception {
//        when(orderService.save(1L, new HashSet<>(Collections.singletonList(1L)))).thenReturn(orderDto);
//        mockMvc.perform(post("/orders/2?certificateIds=2")
//                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
//                .andExpect(status().isCreated());
//    }

    @Test
    @DisplayName("Get all orders by user ID: Given valid user ID, when get all orders, then return the collection of orders")
    void testGetAllOrdersByUserIdShouldReturnOrderCollection() throws Exception {
        when(orderService.getAllByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(Collections.singletonList(orderDto));
        mockMvc.perform(get("/orders/users/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.orderDtoList[0].id").value(1));
    }

    @Test
    @DisplayName("Get all orders: Given pageable information, when get all orders, then return the collection of orders")
    void testGetAllOrdersShouldReturnOrderCollectionWithPagination() throws Exception {
        when(orderService.getAll(any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get("/orders")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.orderDtoList[0].id").value(1));
    }

    @Test
    @DisplayName("Get order by ID: Given valid order ID, when get order by ID, then return the order")
    void testGetOrderByIdShouldReturnOrderWhenItExists() throws Exception {
        when(orderService.getById(1L)).thenReturn(orderDto);
        mockMvc.perform(get("/orders/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Get order details: Given valid order ID and user ID, when get order details, then return the order details")
    void testGetOrderDetailsShouldReturnOrderDetailsWhenTheyExist() throws Exception {
        when(orderService.getUserOrder(1L, 1L)).thenReturn(orderDto);
        mockMvc.perform(get("/orders/1/users/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

}
