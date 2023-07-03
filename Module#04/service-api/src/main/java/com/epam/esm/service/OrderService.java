package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service interface for managing orders.
 */
public interface OrderService {

    /**
     * Get a specific order of a user.
     *
     * @param username the name of user
     * @param userId   the user
     * @param orderId  the order ID
     * @return the order DTO
     */
    OrderDto getUserOrder(String username, Long userId, Long orderId);

    /**
     * Get the most used tags by a user.
     *
     * @param userId the user ID
     * @return the most used tag
     */
    Optional<Tag> getMostUsedTags(Long userId);

    /**
     * Get all orders of a user.
     *
     * @param username the name of user
     * @param userId   the user ID
     * @param pageable the pageable information
     * @return the page of order DTOs
     */
    List<OrderDto> getAllByUserId(String username, Long userId, Pageable pageable);

    /**
     * Create an order for a user with the specified certificate IDs.
     *
     * @param userId         the user ID
     * @param certificateIds the set of certificate IDs
     * @return the created order DTO
     */
    OrderDto createOrder(String username, Long userId, Set<Long> certificateIds);

    /**
     * Get paginated user orders.
     *
     * @param user     the user
     * @param pageable the pageable information
     * @return the page of order DTOs
     */
    Page<OrderDto> getUserOrders(User user, Pageable pageable);

    /**
     * Get all orders with pagination.
     *
     * @param pageable the pageable information
     * @return the page of order DTOs
     */
    Page<OrderDto> getAll(Pageable pageable);

    /**
     * Find a certificate by ID.
     *
     * @param ids the certificate IDs
     * @return the certificate
     */
    List<Certificate> findCertificateById(Set<Long> ids);

    /**
     * Retrieves an {@link OrderDto} based on the specified ID.
     *
     * @param id The ID of the order to retrieve.
     * @return The {@link OrderDto} with the specified ID.
     */
    OrderDto getById(Long id);

    /**
     * Updates an {@link OrderDto} with the provided data.
     *
     * @param dto The {@link OrderDto} containing the updated information.
     * @return The updated {@link OrderDto}.
     */
    OrderDto update(OrderDto dto);

    /**
     * Deletes an {@link OrderDto} based on the specified ID.
     *
     * @param id The ID of the order to delete.
     */
    void delete(Long id);
}
