package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * This interface provides methods for accessing
 * and manipulating orders in the data source.
 */
public interface OrderDao extends Dao<Order> {

    /**
     * Retrieves a list of orders for the specified user,
     * based on the given pagination criteria.
     *
     * @param user     The user for whom to retrieve the orders.
     * @param pageable The pagination criteria.
     * @return A list of orders for the specified user.
     */
    List<Order> getUserOrders(User user, Pageable pageable);

    /**
     * Retrieves an order for the specified user and order ID.
     *
     * @param user    The user for whom to retrieve the order.
     * @param orderId The ID of the order to retrieve.
     * @return An optional containing the order
     * for the specified user and order ID, or empty if not found.
     */
    Optional<Order> getUserOrder(User user, Long orderId);

    /**
     * Retrieves the most used tag by the user with the specified ID.
     *
     * @param userId The ID of the user.
     * @return An optional containing the most used tag by the user,
     * or empty if not found.
     */
    Optional<Tag> getMostUsedTagBy(Long userId);

    /**
     * Retrieves a list of orders for the user with the specified ID.
     *
     * @param userId The ID of the user.
     * @return A list of orders for the user with the specified ID.
     */
    List<Order> findOrdersByUserId(Long userId);
}
