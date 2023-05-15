package com.epam.esm.service;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.CertificateMapper;
import com.epam.esm.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Validated
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final CertificateDao certificateDao;
    private final OrderMapper orderMapper;
    private final CertificateMapper certificateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ)
    public OrderDto save(final Order order) {
        return orderMapper.toDto(orderDao.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getById(Long id) {
        Order order = orderDao.getById(id).orElseThrow(() -> new OrderNotFoundException(
                String.format("Order with id %d not found", id)));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto createOrder(
            final Long userId,
            final List<Long> certificateIds) {
        User user = userDao.getById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id %d not found", userId)));

        List<Certificate> certificates = certificateDao.findAllByIds(certificateIds);

        if (certificates.size() != certificateIds.size()) {
            throw new CertificateNotFoundException("One or more certificates not found");
        }

        BigDecimal cost = certificates.stream()
                .map(Certificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .user(user)
                .certificates(certificates)
                .orderDate(Timestamp.valueOf(LocalDateTime.now()))
                .cost(cost)
                .build();
        Order saved = orderDao.save(order);
        return orderMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getUserOrders(
            final User user,
            final Criteria criteria) {
        return orderDao.getUserOrders(user, criteria)
                .stream()
                .map(orderMapper::toDto)
                .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAll() {
        return orderDao.getAll().stream()
                .map(orderMapper::toDto)
                .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getUserOrder(
            final User user,
            final Long orderId) {
        Order order = orderDao.getUserOrder(user, orderId)
                .orElseThrow(() -> new OrderNotFoundException(
                        String.format("Order not found with id %d", orderId)));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Tag getMostUsedTags(
            final Long userId) {
        return orderDao.getMostUsedTagBy(userId)
                .orElseThrow(() -> new TagNotFoundException(
                        "Tag not found Exception"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateDto> getCertificatesByTags(
            final Criteria criteria) {
        return certificateDao
                .getAll(criteria)
                .stream()
                .map(certificateMapper::toDto)
                .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllByUserId(
            final Long userId) {
        return orderDao.findOrdersByUserId(userId)
                .stream()
                .map(orderMapper::toDto)
                .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Certificate findCertificateById(
            final Long id) {
        return certificateDao.getById(id).orElseThrow(
                () -> new CertificateNotFoundException(
                        "Certificate not found"));
    }
}
