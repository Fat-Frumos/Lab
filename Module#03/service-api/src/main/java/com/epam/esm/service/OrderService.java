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

public interface OrderService {
    OrderDto getUserOrder(User user, Long orderId);

    Tag getMostUsedTags(Long userId);

    Page<CertificateDto> getCertificatesByTags(List<String> tagNames);

    Page<OrderDto> getAllByUserId(Long userId);

    OrderDto createOrder(Long userId, Set<Long> certificateIds);

    Page<OrderDto> getUserOrders(User user, Pageable pageable);

    Page<OrderDto> getAll(Pageable pageable);

    Certificate findCertificateById(Long certificateId);

    OrderDto save(Order order);

    OrderDto getById(Long id);
}
