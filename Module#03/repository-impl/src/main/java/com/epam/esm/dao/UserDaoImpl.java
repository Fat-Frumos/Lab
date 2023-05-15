package com.epam.esm.dao;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.FilterParams;
import com.epam.esm.entity.User;
import com.epam.esm.exception.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    @PersistenceUnit
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public List<User> getAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root);

            Criteria criteria = Criteria.builder().size(25).page(0).filterParams(FilterParams.ID).build();
            TypedQuery<User> typedQuery = entityManager.createQuery(query);
            if (criteria != null) {
                int page = criteria.getPage();
                int size = criteria.getSize();
                if (page >= 0 && size > 0) {
                    typedQuery.setFirstResult(page * size);
                    typedQuery.setMaxResults(size);
                }
            }
            return typedQuery.getResultList();
        }
    }


    @Override
    public Optional<User> getById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.of(entityManager.find(User.class, id));
        }
    }

    @Override
    public Optional<User> getByName(String name) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<User> query = entityManager
                    .createQuery("SELECT u FROM User u WHERE u.username = :name", User.class);
            query.setParameter("name", name);
            List<User> users = query.getResultList();
            return users.isEmpty()
                    ? Optional.empty()
                    : Optional.of(users.get(0));
        }
    }

    @Override
    public User save(User user) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            User saved = entityManager.merge(user);
            complete(transaction);
            return saved;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
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
