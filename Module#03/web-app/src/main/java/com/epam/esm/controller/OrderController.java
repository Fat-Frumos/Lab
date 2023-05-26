package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
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

/**
 * Controller class for handling order-related operations.
 * <p>
 * This class is annotated with {@link RestController} to indicate that it is a Spring MVC controller,
 * and {@link RequestMapping} with a value of "/orders" to map requests to this controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    /**
     * The tag service for performing tag-related operations.
     */
    private final OrderService orderService;

    /**
     * The Order assembler for converting tag entities to DTOs.
     */
    private final OrderAssembler assembler;
    /**
     * The User Service for performing tag-related operations.
     */
    private final UserService userService;

    /**
     * Creates a new order for the specified user and certificate.
     *
     * @param userId        the ID of the user
     * @param certificateId the ID of the certificate
     * @return the created order DTO
     */
    @PostMapping("/{userId}")
    public OrderDto create(
            @PathVariable final Long userId,
            @RequestParam final Long certificateId) {
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

    /**
     * Retrieves all orders for a specific user by user ID.
     *
     * @param userId the ID of the user.
     * @return a collection of order resources.
     */
    @GetMapping("/users/{userId}")
    public CollectionModel<EntityModel<OrderDto>> getAllOrdersByUserId(
            @PathVariable final Long userId) {
        return assembler.toCollectionModel(
                orderService.getAllByUserId(userId));
    }

    /**
     * Retrieves all orders with pagination.
     *
     * @param pageable the pagination information.
     * @return a collection of order resources.
     */
    @GetMapping
    public CollectionModel<EntityModel<OrderDto>> getAllOrders(
            @PageableDefault(size = 25, sort = {"id"},
                    direction = Sort.Direction.ASC) final Pageable pageable) {
        return assembler.toCollectionModel(
                orderService.getAll(pageable));
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order.
     * @return the order resource.
     */
    @GetMapping("/{id}")
    public EntityModel<OrderDto> getOrderById(
            @PathVariable final Long id) {
        return assembler.toModel(
                orderService.getById(id));
    }
}
