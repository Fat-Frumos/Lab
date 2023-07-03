package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.UnauthorizedAccessException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of the OrderService interface.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    /**
     * The data access object for User entities.
     */
    private final UserDao userDao;
    /**
     * The data access object for Order entities.
     */
    private final OrderDao orderDao;
    /**
     * The data access object for Certificate entities.
     */
    private final CertificateDao certificateDao;
    /**
     * The mapper for converting Order entities to DTOs and vice versa.
     */
    private final OrderMapper orderMapper;

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order to retrieve
     * @return the order DTO
     * @throws OrderNotFoundException if the order is not found
     */
    @Override
    @Transactional(readOnly = true)
    public OrderDto getById(final Long id) {
        Order order = orderDao.getById(id).orElseThrow(() ->
                new OrderNotFoundException(
                        String.format("Order with id %d not found", id)));
        return orderMapper.toDto(order);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Creates an order for a user with the specified certificate IDs.
     *
     * @param userId         the ID of the user
     * @param certificateIds the IDs of the certificates
     * @return the created order DTO
     * @throws UserNotFoundException        if the user is not found
     * @throws CertificateNotFoundException if one or more certificates are not found
     */
    @Override
    @Transactional(readOnly = true)
    public OrderDto createOrder(
            final String username,
            final Long userId,
            final Set<Long> certificateIds) {
        User user = getUser(username);
        validUser(user, userId);
        List<Certificate> certificates =
                getCertificatesByIds(certificateIds);
        Order order = getOrder(user, certificates);
        return orderMapper.toDto(orderDao.save(order));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a page of orders for a specific user.
     *
     * @param user     the user
     * @param pageable the pageable object for pagination
     * @return a page of order DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getUserOrders(
            final User user,
            final Pageable pageable) {
        List<OrderDto> dtos = orderMapper.toDtoList(
                orderDao.getUserOrders(user, pageable));
        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a page of all orders.
     *
     * @param pageable the pageable object for pagination
     * @return a page of order DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getAll(
            final Pageable pageable) {
        List<OrderDto> dtos = orderMapper.toDtoList(
                orderDao.getAllBy(pageable));
        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a specific order for a user.
     *
     * @param username
     * @param userId   the ID of the user
     * @param orderId  the ID of the order
     * @return the order DTO
     * @throws OrderNotFoundException if the order is not found
     */
    @Override
    @Transactional(readOnly = true)
    public OrderDto getUserOrder(
            String username, final Long userId, final Long orderId) {
        Order order = orderDao.getUserOrder(userId, orderId)
                .orElseThrow(() -> new OrderNotFoundException(
                        String.format("Order not found with id %d", orderId)));
        return orderMapper.toDto(order);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves the most used tag by a user.
     *
     * @param userId the ID of the user
     * @return the most used tag
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> getMostUsedTags(
            final Long userId) {
        return orderDao.getMostUsedTagBy(userId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all orders for a specific user.
     *
     * @param userId   the ID of the user
     * @param pageable the pageable object for pagination
     * @return a page of order DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllByUserId(
            final String username,
            final Long userId,
            final Pageable pageable) {
        User user = getUser(username);
        validUser(user, userId);
        return orderMapper.toDtoList(
                orderDao.findOrdersByUserId(userId, pageable));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a certificate by its ID.
     *
     * @param ids the ID of the certificate
     * @return the certificate
     * @throws CertificateNotFoundException if the certificate is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<Certificate> findCertificateById(
            final Set<Long> ids) {
        return certificateDao.findAllByIds(ids);
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * @param dto The {@link OrderDto} containing the updated information.
     * @return The updated {@link OrderDto}.
     */
    @Override
    @Transactional
    public OrderDto update(
            final OrderDto dto) {
        return orderMapper.toDto(orderDao.update(
                orderMapper.toEntity(dto)));
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * @param id of the order to delete.
     */
    @Override
    @Transactional
    public void delete(final Long id) {
        orderDao.delete(id);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user
     * @return the User object if found
     * @throws UserNotFoundException if no user is found with the given username
     */
    private User getUser(final String username) {
        return userDao.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("%s User not found%n", username)));
    }

    /**
     * Constructs an Order object using the provided user and list of certificates.
     *
     * @param user         the User object associated with the order
     * @param certificates the list of certificates included in the order
     * @return the constructed Order object
     */
    private static Order getOrder(
            final User user,
            final List<Certificate> certificates) {
        return Order.builder()
                .user(user)
                .certificates(new HashSet<>(certificates))
                .orderDate(Timestamp.valueOf(LocalDateTime.now()))
                .cost(certificates.stream()
                        .map(Certificate::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }

    /**
     * Retrieves a list of certificates based on the given set of certificate IDs.
     *
     * @param certificateIds the set of certificate IDs to retrieve
     * @return the list of certificates matching the given IDs
     * @throws CertificateNotFoundException if one or more certificates are not found
     */
    private List<Certificate> getCertificatesByIds(
            final Set<Long> certificateIds) {
        List<Certificate> certificates = certificateDao
                .findAllByIds(certificateIds);
        if (certificates.size() != certificateIds.size()) {
            throw new CertificateNotFoundException(
                    "One or more certificates not found");
        }
        return certificates;
    }

    /**
     * Method will throw an exception when the parameter is null.
     * Checks if the authenticated user is the same as the specified userId.
     *
     * @param user   The authenticated user obtained from the security context.
     * @param userId The ID of the user to compare with the authenticated user.
     * @throws UnauthorizedAccessException If the authenticated user is null
     *                                     or has a different ID than the specified userId.
     */
    private static void validUser(
            final User user, final Long userId) {
        if (user.getId().compareTo(userId) != 0) {
            throw new UnauthorizedAccessException(
                    "Access denied for " + user.getUsername());
        }
    }
}
