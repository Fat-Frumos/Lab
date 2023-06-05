package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@SpringJUnitConfig(classes = TestConfig.class)
class OrderControllerTest {
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
        mockMvc.perform(get("/orders/users/1/most"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Summer"));
    }
}
