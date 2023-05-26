package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * Service interface for managing orders.
 */
public interface OrderService {

    /**
     * Get a specific order of a user.
     *
     * @param user    the user
     * @param orderId the order ID
     * @return the order DTO
     */
    OrderDto getUserOrder(User user, Long orderId);

    /**
     * Get the most used tags by a user.
     *
     * @param userId the user ID
     * @return the most used tag
     */
    Tag getMostUsedTags(Long userId);

    /**
     * Get a page of certificates based on tag names.
     *
     * @param tagNames the list of tag names
     * @return the page of certificate DTOs
     */
    Page<CertificateDto> getCertificatesByTags(List<String> tagNames);

    /**
     * Get all orders of a user.
     *
     * @param userId the user ID
     * @return the page of order DTOs
     */
    Page<OrderDto> getAllByUserId(Long userId);

    /**
     * Create an order for a user with the specified certificate IDs.
     *
     * @param userId         the user ID
     * @param certificateIds the set of certificate IDs
     * @return the created order DTO
     */
    OrderDto createOrder(Long userId, Set<Long> certificateIds);

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
     * @param certificateId the certificate ID
     * @return the certificate
     */
    Certificate findCertificateById(Long certificateId);

    /**
     * Save an order.
     *
     * @param order the order to save
     * @return the saved order DTO
     */
    OrderDto save(Order order);

    /**
     * Get an order by ID.
     *
     * @param id the order ID
     * @return the order DTO
     */
    OrderDto getById(Long id);
}
