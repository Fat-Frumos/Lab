package com.epam.esm.dao;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends Dao<Order> {
    List<Order> getUserOrders(User user, Criteria criteria);

    Optional<Order> getUserOrder(User user, Long orderId);

    Optional<Tag> getMostUsedTagBy(Long userId);

    List<Order> findOrdersByUserId(Long userId);
}
