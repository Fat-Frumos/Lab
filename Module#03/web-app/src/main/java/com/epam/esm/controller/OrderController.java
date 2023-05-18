package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.FilterParams;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.SortOrder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderAssembler assembler;
    private final UserService userService;

    @PostMapping("/{userId}")
    public OrderDto create(
            @PathVariable final Long userId,
            @RequestParam final Long certificateId) {  // TODO list certificates Ids
        User user = userService.findById(userId);
        Certificate certificate = orderService
                .findCertificateById(certificateId);
        Order order = Order.builder()
                .certificates(Collections.singleton(certificate))
                .cost(certificate.getPrice())
                .user(user)
                .orderDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        return orderService.save(order);
    }

    @GetMapping("/users/{userId}")
    public CollectionModel<EntityModel<OrderDto>> getAllOrdersByUserId(
            @PathVariable final Long userId) { // TODO criteria
        return assembler.toCollectionModel(
                orderService.getAllByUserId(userId));
    }

    @GetMapping("")
    public CollectionModel<EntityModel<OrderDto>> getAllOrders(
            @RequestParam(defaultValue = "UNSORTED") SortOrder sort,
            @RequestParam(defaultValue = "ID") FilterParams params,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return assembler.toCollectionModel(
                orderService.getAll(Criteria.builder()
                        .filterParams(params)
                        .sortOrder(sort)
                        .page(page)
                        .size(size)
                        .build()));
    }

    @GetMapping("/{id}")
    public EntityModel<OrderDto> getOrderById(
            @PathVariable final Long id) {
        return assembler.toModel(orderService.getById(id));
    }

    @GetMapping("/certificates/{id}")
    public CollectionModel<EntityModel<OrderDto>> getAllOrdersByCertificateId(
            @PathVariable Long id) {
        return assembler.toCollectionModel(
                orderService.getAllByUserId(id));
    }
}
