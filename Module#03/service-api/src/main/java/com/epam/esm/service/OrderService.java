package com.epam.esm.service;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.Set;

public interface OrderService {
    OrderDto getUserOrder(
            User user,
            Long orderId);

    Tag getMostUsedTags(
            Long userId);

    List<CertificateDto> getCertificatesByTags(List<String> tagNames);

    List<OrderDto> getAllByUserId(Long userId);

    OrderDto createOrder(
            Long userId,
            Set<Long> certificateIds);

    List<OrderDto> getUserOrders(User user, Criteria criteria);

    List<OrderDto> getAll(Criteria criteria);

    Certificate findCertificateById(Long certificateId);

    OrderDto save(Order order);

    OrderDto getById(Long id);
}
