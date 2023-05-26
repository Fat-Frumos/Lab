package com.epam.esm.dao;

import com.epam.esm.entity.User;
import com.epam.esm.exception.UserAlreadyExistsException;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.dao.Queries.SELECT_USER_BY_NAME;

/**
 * The implementation of the UserDao interface.
 * <p>
 * This class provides the concrete implementation
 * for accessing and manipulating users in the database.
 * <p>
 * It utilizes the EntityManagerFactory and EntityManager
 * to interact with the database.
 */
@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    /**
     * The entity manager factory used for obtaining the entity manager.
     */
    @PersistenceUnit
    private final EntityManagerFactory factory;

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all users with pagination.
     *
     * @param pageable the pagination information
     * @return a list of user entities
     */
    @Override
    public List<User> getAllBy(final Pageable pageable) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);

            EntityGraph<User> graph = entityManager.createEntityGraph(User.class);
            graph.addAttributeNodes("orders");

            query.select(root);

            return entityManager.createQuery(query)
                    .setHint("jakarta.persistence.fetchgraph", graph)
                    .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user
     * @return an {@link Optional} containing the user entity,
     * or empty if not found
     */
    @Override
    public Optional<User> getById(final Long id) {
        try (EntityManager entityManager =
                     factory.createEntityManager()) {
            return Optional.of(entityManager.find(User.class, id));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a user by its name.
     *
     * @param name the name of the user
     * @return an {@link Optional} containing the user entity,
     * or empty if not found
     */
    @Override
    public Optional<User> getByName(final String name) {
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

    /**
     * {@inheritDoc}
     * <p>
     * Saves a user.
     *
     * @param user the user to save
     * @return the saved user entity
     * @throws UserAlreadyExistsException if the user already exists
     */
    @Override
    public User save(final User user) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                boolean empty = entityManager
                        .createQuery(SELECT_USER_BY_NAME, User.class)
                        .setParameter("name", user.getUsername())
                        .getResultList().isEmpty();
                if (!empty) {
                    throw new UserAlreadyExistsException("User is Already Exists");
                }
                entityManager.persist(user);
                transaction.commit();
                return user;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new PersistenceException(e.getMessage(), e);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deletes a user by its ID.
     *
     * @param id the ID of the user to delete
     * @throws PersistenceException if an error occurs during the deletion
     */
    @Override
    public void delete(final Long id) {
        try (EntityManager entityManager = factory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                User user = entityManager.getReference(User.class, id);
                entityManager.remove(user);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new PersistenceException(e);
            }
        }
    }
}
