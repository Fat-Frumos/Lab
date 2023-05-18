package com.epam.esm.dao;

import com.epam.esm.criteria.Criteria;
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
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.criteria.CertificateQueries.SELECT_ORDER_BY_NAME;

@Repository
@RequiredArgsConstructor
public class OrderDaoImpl implements OrderDao {

    @PersistenceUnit
    private final EntityManagerFactory factory;

    @Override
    public List<Order> getAll(Criteria criteria) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Order> query = builder.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            query.select(root);

            EntityGraph<Order> entityGraph = entityManager.createEntityGraph(Order.class);
            entityGraph.addAttributeNodes("user");
            entityGraph.addAttributeNodes("certificates");
            entityGraph.addSubgraph("certificates").addAttributeNodes("tags");

            return entityManager.createQuery(query)
                    .setHint("jakarta.persistence.fetchgraph", entityGraph)
                    .setFirstResult(criteria.getSize() * criteria.getPage())
                    .setMaxResults(criteria.getSize())
                    .getResultList();
        }
    }

    @Override
    public Optional<Order> getById(final Long id) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            return Optional.of(entityManager.find(Order.class, id));
        }
    }

    @Override
    public Optional<Order> getByName(final String name) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            TypedQuery<Order> query = entityManager.createQuery(
                    SELECT_ORDER_BY_NAME,
                    Order.class);
            query.setParameter("username", name);
            List<Order> orders = query.getResultList();
            return orders.isEmpty() ? Optional.empty() : Optional.of(orders.get(0));
        }
    }

    @Override
    public Order save(final Order order) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            User managedUser = entityManager.find(User.class, order.getUser().getId());

            if (managedUser == null) {
                throw new EntityNotFoundException("User not found");
            }
            order.setUser(managedUser);

            Set<Certificate> managedCertificates = new HashSet<>();

            for (Certificate certificate : order.getCertificates()) {
                Certificate managedCertificate = entityManager.find(Certificate.class, certificate.getId());
                if (managedCertificate == null) {
                    throw new EntityNotFoundException("Certificate not found: " + certificate.getId());
                }
                managedCertificates.add(managedCertificate);
            }
            order.setCertificates(managedCertificates);
            entityManager.persist(order);
            complete(transaction);
        }
        return order;
    }

    @Override
    public void delete(final Long id) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            Order order = entityManager.find(Order.class, id);
            if (order != null) {
                entityManager.remove(order);
                complete(transaction);
            } else {
                throw new OrderNotFoundException("Order Not Found" + id);
            }
        }
    }

    public List<Order> getUserOrders(
            final User user,
            final Criteria criteria) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Order> query = builder.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);

            EntityGraph<Order> graph = entityManager.createEntityGraph(Order.class);
            graph.addAttributeNodes("user");
            graph.addSubgraph("certificate").addAttributeNodes("tags");

            query.where(builder.equal(root.get("user"), user));

            return entityManager.createQuery(query)
                    .setHint("jakarta.persistence.fetchgraph", graph)
                    .setMaxResults(criteria.getSize())
                    .setFirstResult(criteria.getPage() * criteria.getSize())
                    .getResultList();
        }
    }

    public Optional<Order> getUserOrder(
            final User user,
            final Long orderId) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Order> query = builder.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            query.select(root).where(builder.equal(root.get("user"), user), builder.equal(root.get("id"), orderId));
            List<Order> results = entityManager.createQuery(query).getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        }
    }

    public Optional<Tag> getMostUsedTagBy(
            final Long userId) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = builder.createTupleQuery();
            Root<Order> root = query.from(Order.class);
            Join<Order, Tag> tagJoin = root.join("tags");
            query.multiselect(tagJoin, builder.count(tagJoin)).where(builder.equal(root.get("user").get("id"), userId));
            query.groupBy(tagJoin);
            query.orderBy(builder.desc(builder.count(tagJoin)));
            List<Tuple> results = entityManager.createQuery(query).setMaxResults(1).getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0).get(0, Tag.class));
        }
    }

    public List<Order> findOrdersByUserId(
            final Long userId) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Order> query = builder.createQuery(Order.class);
            Root<Order> root = query.from(Order.class);
            query.select(root).where(builder.equal(root.get("user").get("id"), userId));
            return entityManager.createQuery(query).getResultList();
        }
    }

    private static void complete(EntityTransaction transaction) {
        Objects.requireNonNull(transaction);
        try {
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new EntityNotFoundException(e.getMessage(), e);
        }
    }
}
