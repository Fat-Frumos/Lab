package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;

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
     * Creates a new order for the specified user and certificate.
     *
     * @param userId         the ID of the user
     * @param certificateIds the ID of the certificate
     * @return the created order DTO
     */
    @PostMapping("/{userId}")
    @ResponseStatus(CREATED)
    public EntityModel<OrderDto> create(
            @PathVariable final Long userId,
            @RequestParam final Set<Long> certificateIds) {
        return assembler.toModel(
                orderService.save(userId, certificateIds));
    }

    /**
     * Retrieves all orders for a specific user by user ID.
     *
     * @param userId the ID of the user.
     * @param pageable the pagination information.
     * @return a collection of order resources.
     */
    @GetMapping("/users/{userId}")
    public CollectionModel<EntityModel<OrderDto>> getAllOrdersByUserId(
            @PathVariable final Long userId,
            @PageableDefault(size = 25, sort = {"id"},
                    direction = Sort.Direction.ASC) final Pageable pageable) {
        return assembler.toCollectionModel(
                orderService.getAllByUserId(userId, pageable));
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

    /**
     * Retrieves the most used tag for a specific user.
     *
     * @param userId the ID of the user
     * @return the ResponseEntity with the most used tag if found,
     * or a not found response if not found
     */
    @GetMapping("/users/{userId}/most")
    public ResponseEntity<Tag> getMostUsedTag(
            final @PathVariable Long userId) {
        Optional<Tag> mostUsedTag =
                orderService.getMostUsedTags(userId);
        return mostUsedTag.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves the details of an order for a specific user.
     *
     * @param orderId the ID of the order
     * @param userId  the ID of the user
     * @return the EntityModel containing the order details
     */
    @GetMapping("/{orderId}/users/{userId}")
    public EntityModel<OrderDto> getOrderDetails(
            final @PathVariable Long orderId,
            final @PathVariable Long userId) {
        return assembler.toModel(
                orderService.getUserOrder(orderId, userId));
    }
}
