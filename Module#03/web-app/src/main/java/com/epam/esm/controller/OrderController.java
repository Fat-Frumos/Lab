package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;
    private final OrderAssembler assembler;
    private final UserService userService;

    @PostMapping("/orders/{userId}")
    public OrderDto create(
            @PathVariable final Long userId,
            @RequestParam final Long certificateId) {
        User user = userService.findById(userId);
        Certificate certificate = orderService
                .findCertificateById(certificateId);
        Order order = Order.builder()
                .certificates(Collections.singletonList(certificate))
                .cost(certificate.getPrice())
                .user(user)
                .orderDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        return orderService.save(order);
    }

    @GetMapping("/orders/{userId}")
    public List<OrderDto> getAllOrdersByUserId(
            @PathVariable final Long userId) { // TODO criteria
        return orderService.getAllByUserId(userId);
    }

    @GetMapping("orders")
    public List<OrderDto> getAllOrders() {
        return orderService.getAll();
    }


    @GetMapping("orders/{id}")
    public EntityModel<OrderDto> getOrderById(
            @PathVariable final Long id) {
        return assembler.toModel(orderService.getById(id));
    }

    @GetMapping("/certificates/{id}")
    public List<OrderDto> getAllOrdersByCertificateId(
            @PathVariable Long id) {
        return orderService.getAllByUserId(id);
    }
}
