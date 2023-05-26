package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.OrderNotFoundException;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.dao.Queries.SELECT_ORDER_BY_ID;
import static com.epam.esm.dao.Queries.SELECT_ORDER_BY_NAME;

/**
 * Implementation of the {@link OrderDao} interface
 * for managing orders in the data access layer.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderDaoImpl implements OrderDao {
    /**
     * Constant for the fetch graph hint used in entity manager queries.
     */
    private static final String FETCH_GRAPH =
            "jakarta.persistence.fetchgraph";

    /**
     * The entity manager factory used for obtaining the entity manager.
     */
    @PersistenceUnit
    private final EntityManagerFactory factory;

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all orders with pagination.
     *
     * @param pageable the pagination information.
     * @return a list of order entities.
     */
    public List<Order> getAllBy(final Pageable pageable) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Order> query = builder.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            query.select(root);
            return entityManager.createQuery(query)
                    .setHint(FETCH_GRAPH, entityManager
                            .getEntityGraph("Order.certificates"))
                    .setFirstResult(pageable.getPageNumber()
                            * pageable.getPageSize())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param id the ID of the order.
     * @return an optional containing the order entity,
     * or empty if not found.
     */
    @Override
    public Optional<Order> getById(final Long id) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            return entityManager.createQuery(SELECT_ORDER_BY_ID, Order.class)
                    .setParameter("id", id)
                    .setHint(FETCH_GRAPH,
                            entityManager.getEntityGraph("Order.certificates"))
                    .getResultStream()
                    .findFirst();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves an order by its name (username).
     *
     * @param name the name of the order.
     * @return an optional containing the order entity, or empty if not found.
     */
    @Override
    public Optional<Order> getByName(final String name) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            TypedQuery<Order> query = entityManager
                    .createQuery(SELECT_ORDER_BY_NAME,
                            Order.class);
            query.setParameter("username", name);
            List<Order> orders = query.getResultList();
            return orders.isEmpty()
                    ? Optional.empty()
                    : Optional.of(orders.get(0));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param order the order entity to be saved.
     * @return the saved order entity.
     */
    @Override
    public Order save(final Order order) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            EntityTransaction transaction =
                    entityManager.getTransaction();
            try {
                transaction.begin();
                User managedUser = entityManager
                        .find(User.class, order.getUser().getId());
                if (managedUser == null) {
                    throw new EntityNotFoundException(
                            "User not found");
                }
                order.setUser(managedUser);

                Set<Certificate> certificates = new HashSet<>();

                for (Certificate certificate : order.getCertificates()) {
                    Certificate managedCertificate = entityManager
                            .find(Certificate.class, certificate.getId());
                    if (managedCertificate == null) {
                        throw new EntityNotFoundException(
                                String.format("Certificate not found: %d",
                                        certificate.getId()));
                    }
                    certificates.add(managedCertificate);
                }
                order.setCertificates(certificates);
                entityManager.persist(order);
                transaction.commit();
                return order;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new PersistenceException(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param id the ID of the order to be deleted.
     */
    @Override
    public void delete(final Long id) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            EntityTransaction transaction =
                    entityManager.getTransaction();
            try {
                transaction.begin();
                Order order = entityManager
                        .getReference(Order.class, id);
                if (order == null) {
                    throw new OrderNotFoundException(
                            "Order Not Found" + id);
                }
                entityManager.remove(order);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new EntityNotFoundException(
                        e.getMessage(), e);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all orders for a specific user with pagination.
     *
     * @param user     the user entity.
     * @param pageable the pagination information.
     * @return a list of order entities.
     */
    public List<Order> getUserOrders(
            final User user,
            final Pageable pageable) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager
                    .getCriteriaBuilder();
            CriteriaQuery<Order> query = builder
                    .createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            EntityGraph<Order> graph = entityManager
                    .createEntityGraph(Order.class);
            graph.addAttributeNodes("user");
            graph.addSubgraph("certificate")
                    .addAttributeNodes("tags");

            query.where(builder.equal(root.get("user"), user));

            return entityManager.createQuery(query)
                    .setHint(FETCH_GRAPH, graph)
                    .setMaxResults(pageable.getPageSize())
                    .setFirstResult(pageable.getPageNumber()
                            * pageable.getPageSize())
                    .getResultList();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves an order for a specific user by order ID.
     *
     * @param user    the user entity.
     * @param orderId the ID of the order.
     * @return an optional containing the order entity,
     * or empty if not found.
     */
    public Optional<Order> getUserOrder(
            final User user,
            final Long orderId) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            CriteriaBuilder builder =
                    entityManager.getCriteriaBuilder();
            CriteriaQuery<Order> query =
                    builder.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            query.select(root).where(
                    builder.equal(root.get("user"), user),
                    builder.equal(root.get("id"), orderId));
            List<Order> results = entityManager
                    .createQuery(query)
                    .getResultList();
            return results.isEmpty()
                    ? Optional.empty()
                    : Optional.of(results.get(0));
        }
    }

    /**
     * <p>
     * Retrieves the most used tag by a specific user.
     *
     * @param userId the ID of the user.
     * @return an optional containing
     * the most used tag entity, or empty if not found.
     */
    public Optional<Tag> getMostUsedTagBy(final Long userId) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            CriteriaBuilder builder =
                    entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query =
                    builder.createTupleQuery();
            Root<Order> root = query.from(Order.class);
            Join<Order, Tag> tagJoin = root.join("tags");
            query.multiselect(tagJoin, builder.count(tagJoin))
                    .where(builder.equal(
                            root.get("user").get("id"),
                            userId));
            query.groupBy(tagJoin);
            query.orderBy(builder.desc(builder.count(tagJoin)));
            List<Tuple> results = entityManager
                    .createQuery(query)
                    .setMaxResults(1)
                    .getResultList();
            return results.isEmpty()
                    ? Optional.empty()
                    : Optional.of(results.get(0).get(0, Tag.class));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Finds all orders for a specific user by user ID.
     *
     * @param userId the ID of the user.
     * @return a list of order entities.
     */
    public List<Order> findOrdersByUserId(final Long userId) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            CriteriaBuilder builder =
                    entityManager.getCriteriaBuilder();
            CriteriaQuery<Order> query =
                    builder.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            query.select(root).where(builder.equal(
                    root.get("user").get("id"),
                    userId));
            return entityManager
                    .createQuery(query)
                    .getResultList();
        }
    }
}
