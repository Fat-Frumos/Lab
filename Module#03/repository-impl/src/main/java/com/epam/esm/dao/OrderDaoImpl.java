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
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.esm.dao.Queries.CERTIFICATES;
import static com.epam.esm.dao.Queries.FETCH_GRAPH;
import static com.epam.esm.dao.Queries.SELECT_ORDER_BY_ID;
import static com.epam.esm.dao.Queries.SELECT_ORDER_BY_NAME;
import static com.epam.esm.dao.Queries.TAGS;

/**
 * Implementation of the {@link OrderDao} interface
 * for managing orders in the data access layer.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderDaoImpl implements OrderDao {
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
        try (EntityManager entityManager = factory
                .createEntityManager()) {
            CriteriaQuery<Order> query = entityManager
                    .getCriteriaBuilder()
                    .createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            root.fetch(CERTIFICATES, JoinType.LEFT)
                    .fetch(TAGS, JoinType.LEFT);
            root.fetch("user", JoinType.LEFT);
            query.select(root);
            return entityManager.createQuery(query)
                    .setHint(FETCH_GRAPH, entityManager
                            .getEntityGraph("Order.certificates.tags"))
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
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            return entityManager.createQuery(
                            SELECT_ORDER_BY_ID, Order.class)
                    .setParameter("id", id)
                    .setHint(FETCH_GRAPH, entityManager
                            .getEntityGraph("Order.certificates.tags"))
                    .getResultStream()
                    .findFirst();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves an order by its username.
     *
     * @param username the name of the order.
     * @return an optional containing the order entity, or empty if not found.
     */
    @Override
    public Optional<Order> getByName(final String username) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            TypedQuery<Order> query = entityManager
                    .createQuery(SELECT_ORDER_BY_NAME,
                            Order.class);
            query.setParameter("username", username);
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
    public Order save(Order order) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                order.setUser(entityManager
                        .getReference(User.class, order.getUser().getId()));
                order.setCertificates(order.getCertificates()
                        .stream()
                        .map(certificate -> entityManager
                                .getReference(Certificate.class, certificate.getId()))
                        .collect(Collectors.toSet()));
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
                    .addAttributeNodes(TAGS);

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
     * @param userId  the user ID.
     * @param orderId the ID of the order.
     * @return an optional containing the order entity,
     * or empty if not found.
     */
    public Optional<Order> getUserOrder(
            final Long userId, final Long orderId) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            EntityGraph<Order> graph = entityManager
                    .createEntityGraph(Order.class);
            graph.addAttributeNodes("user", CERTIFICATES, "certificates.tags");

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();

            CriteriaQuery<Order> query = builder.createQuery(Order.class);

            Root<Order> root = query.from(Order.class);
            query.select(root).where(
                    builder.equal(root.get("user").get("id"), userId),
                    builder.equal(root.get("id"), orderId));

            List<Order> results = entityManager
                    .createQuery(query)
                    .setHint(FETCH_GRAPH, graph)
                    .getResultList();
            return results.isEmpty()
                    ? Optional.empty()
                    : Optional.of(results.get(0));
        }
    }

    /**
     * Retrieves the most used tag by a specific user.
     *
     * @param userId the ID of the user.
     * @return an optional containing
     * the most used tag entity, or empty if not found.
     */
    public Optional<Tag> getMostUsedTagBy(
            final Long userId) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();
            Root<Order> root = query.from(Order.class);
            Join<Order, User> userJoin = root.join("user");
            Join<Order, Certificate> certificateJoin = root.join("certificates");
            Join<Certificate, Tag> tagJoin = certificateJoin.join("tags");

            query.multiselect(tagJoin.alias("tag"), builder.sum(root.get("cost")).alias("totalCost"))
                    .groupBy(tagJoin)
                    .orderBy(builder.desc(builder.sum(root.get("cost"))));

            Predicate userPredicate = builder.equal(userJoin.get("id"), userId);
            query.where(userPredicate);
            TypedQuery<Tuple> typedQuery = entityManager.createQuery(query).setMaxResults(1);
            List<Tuple> resultList = typedQuery.getResultList();

            return resultList.isEmpty()
                    ? Optional.empty()
                    : Optional.ofNullable(resultList.get(0).get("tag", Tag.class));
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
    public List<Order> findOrdersByUserId(Long userId) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            EntityGraph<Order> graph =
                    entityManager.createEntityGraph(Order.class);
            graph.addAttributeNodes(CERTIFICATES);
            graph.addSubgraph(CERTIFICATES).addAttributeNodes(TAGS);
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Order> query = builder.createQuery(Order.class);
            query.where(builder.equal(
                    query.from(Order.class).get("user").get("id"), userId));
            return entityManager
                    .createQuery(query)
                    .setHint(FETCH_GRAPH, graph)
                    .getResultList();
        }
    }
}
