package com.epam.esm.dao;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.User;
import com.epam.esm.exception.UserAlreadyExistsException;
import com.epam.esm.exception.UserNotFoundException;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.epam.esm.criteria.CertificateQueries.SELECT_USER_BY_NAME;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    @PersistenceUnit
    private final EntityManagerFactory factory;

    @Override
    public List<User> getAll(final Criteria criteria) {
        try (EntityManager entityManager = factory.createEntityManager()) {

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);

            EntityGraph<User> graph = entityManager.createEntityGraph(User.class);
            graph.addAttributeNodes("orders");

            query.select(root);

            return entityManager.createQuery(query)
                    .setHint("jakarta.persistence.fetchgraph", graph)
                    .setFirstResult(criteria.getPage() * criteria.getSize())
                    .setMaxResults(criteria.getSize())
                    .getResultList();
        }
    }


    @Override
    public Optional<User> getById(
            final Long id) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            return Optional.of(entityManager.find(User.class, id));
        }
    }

    @Override
    public Optional<User> getByName(String name) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {

            List<User> users = entityManager
                    .createQuery(SELECT_USER_BY_NAME, User.class)
                    .setParameter("name", name)
                    .getResultList();

            return users.isEmpty()
                    ? Optional.empty()
                    : Optional.of(users.get(0));
        }
    }

    @Override
    public User save(User user) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            boolean empty = entityManager
                    .createQuery(SELECT_USER_BY_NAME, User.class)
                    .setParameter("name", user.getUsername())
                    .getResultList().isEmpty();
            if (empty) {
                entityManager.persist(user);
                complete(transaction);
                return user;
            } else {
                throw new UserAlreadyExistsException("User is Already Exists");
            }

        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user);
            } else {
                throw new UserNotFoundException("Order Not Found" + id);
            }
            complete(transaction);
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
