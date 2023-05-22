package com.epam.esm.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final CertificateDao certificateDao;
    private final OrderMapper orderMapper;
    private final CertificateMapper certificateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public OrderDto save(final Order order) {
        return orderMapper.toDto(orderDao.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getById(Long id) {
        Order order = orderDao.getById(id)
                .orElseThrow(() -> new OrderNotFoundException(
                        String.format("Order with id %d not found", id)));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto createOrder(
            final Long userId,
            final Set<Long> certificateIds) {
        User user = userDao.getById(userId) // TODO
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id %d not found", userId)));
        Set<Certificate> certificates = certificateDao.findAllByIds(certificateIds); // TODO
        if (certificates.size() != certificateIds.size()) {
            throw new CertificateNotFoundException("One or more certificates not found");
        }
        Order order = Order.builder()
                .user(user)
                .certificates(certificates)
                .orderDate(Timestamp.valueOf(LocalDateTime.now()))
                .cost(certificates.stream()
                        .map(Certificate::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
        Order saved = orderDao.save(order);
        return orderMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getUserOrders(
            final User user,
            final Pageable pageable) {
        List<OrderDto> dtos = orderMapper.toDtoList(
                orderDao.getUserOrders(user, pageable));
        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getAll(
            final Pageable pageable) {
        List<OrderDto> dtos = orderMapper.toDtoList(
                orderDao.getAll(pageable));
        return new PageImpl<>(dtos, pageable, dtos.size());
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
    public Page<CertificateDto> getCertificatesByTags(
            final List<String> tagNames) {
        List<CertificateDto> dtos = certificateMapper.toDtoList(
                certificateDao.findByTagNames(tagNames));
        return new PageImpl<>(dtos, Pageable.unpaged(), dtos.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getAllByUserId(final Long userId) {
        List<OrderDto> dtos = orderMapper.toDtoList(
                orderDao.findOrdersByUserId(userId));
        return new PageImpl<>(dtos, Pageable.unpaged(), dtos.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Certificate findCertificateById(
            final Long id) {
        return certificateDao.getById(id)
                .orElseThrow(() -> new CertificateNotFoundException(
                        "Certificate not found"));
    }
}
