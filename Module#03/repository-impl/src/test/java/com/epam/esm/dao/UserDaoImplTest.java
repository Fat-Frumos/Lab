package com.epam.esm.dao;

import com.epam.esm.entity.User;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.dao.Queries.FETCH_GRAPH;
import static com.epam.esm.dao.Queries.SELECT_USER_BY_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDaoImplTest {
    @InjectMocks
    private UserDaoImpl userDao;
    @Mock
    private EntityManagerFactory entityManagerFactory;
    @Mock
    private EntityTransaction transaction;
    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<User> typedQuery;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private CriteriaQuery<User> criteriaQuery;
    @Mock
    private Root<User> root;
    @Mock
    private EntityGraph<User> graph;
    private final Pageable pageable = PageRequest.of(0, 25, Sort.by("name").ascending());
    private final Long id = 1L;
    private final User user = User.builder().id(id).username("Spring").email("spring@i.ua").build();

    User entity = mock(User.class);

//    @ParameterizedTest
//    @DisplayName("Get User by id")
//    @CsvSource({
//            "1, Olivia, Noah, Olivia-Noah@gmail.com",
//            "2, Emma, Liam, Emma-Liam@gmail.com",
//            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com",
//            "4, Amelia, Elijah, Amelia-Elijah@gmail.com",
//            "5, Ava, Leo, Ava-Leo@gmail.com"
//    })
//    void testGetUserOrders(Long userId, String firstName, String lastName, String email) {
//
//        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
//        when(entityManager.createEntityGraph(User.class)).thenReturn(graph);
//        User user = User.builder()
//                .id(userId)
//                .username(firstName + "-" + lastName)
//                .email(email)
//                .build();
//        when(entityManager.find(eq(User.class), eq(userId), anyMap())).thenReturn(user);
//        Optional<User> optional = userDao.getById(userId);
//
//        assertTrue(optional.isPresent());
//        assertEquals(user.getId(), optional.get().getId());
//        assertEquals(user.getUsername(), optional.get().getUsername());
//        verify(entityManager).find(eq(User.class), eq(userId), anyMap());
//
//    }


    @Test
    @DisplayName("Test delete method")
    void testDelete() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.getReference(User.class, id)).thenReturn(entity);
        userDao.delete(id);
        verify(entityManager).getReference(User.class, id);
        verify(entityManager).remove(entity);
        verify(transaction).commit();
        verify(entityManager).close();
    }

    @Test
    @DisplayName("Test delete method with PersistenceException")
    void testDeleteWithPersistenceException() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        User entity = mock(User.class);
        when(entityManager.getReference(User.class, id)).thenReturn(entity);
        doThrow(RuntimeException.class).when(entityManager).remove(entity);
        when(transaction.isActive()).thenReturn(true);
        assertThrows(PersistenceException.class, () -> userDao.delete(id));
        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(entityManager).getReference(User.class, id);
        verify(entityManager).remove(entity);
        verify(transaction).rollback();
        verify(transaction).isActive();
        verify(entityManager).close();
    }

    @ParameterizedTest
    @DisplayName("Test getAll method")
    @CsvSource({
            "1, Olivia, Noah, Olivia@i.ua, Noah@gmail.com",
            "2, Emma, Liam, Emma@i.ua, Liam@gmail.com",
            "3, Charlotte, Oliver, Charlotte@i.ua, Oliver@gmail.com",
            "4, Amelia, Elijah, Amelia@i.ua, Elijah@gmail.com",
            "5, Ava, Leo, Ava@i.ua, Leo@gmail.com"
    })
    void getAllTest(Long id1, String username1, String username2, String email1, String email2) {

        List<User> users = Arrays.asList(
                User.builder().id(id1).username(username1).email(email1).orders(new HashSet<>()).build(),
                User.builder().id(id1 + 10).username(username2).email(email2).orders(new HashSet<>()).build()
        );
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(User.class)).thenReturn(root);
        when(entityManager.createEntityGraph(User.class)).thenReturn(graph);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.setHint(FETCH_GRAPH, graph)).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(0)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(users);

        List<User> result = userDao.getAllBy(pageable);

        assertEquals(users, result);
        verify(entityManagerFactory).createEntityManager();
        verify(entityManager).getCriteriaBuilder();
        verify(criteriaBuilder).createQuery(User.class);
        verify(criteriaQuery).from(User.class);
        verify(entityManager).createEntityGraph(User.class);
        verify(criteriaQuery).select(root);
        verify(entityManager).createQuery(criteriaQuery);
        verify(typedQuery).setHint(FETCH_GRAPH, graph);
        verify(typedQuery).getResultList();
        verify(entityManager).close();
    }

    @ParameterizedTest
    @DisplayName("Test get user by name")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com",
            "2, Emma, Liam, Emma-Liam@gmail.com",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com",
            "5, Ava, Leo, Ava-Leo@gmail.com"
    })
    void testGetByName(Long userId, String firstName, String lastName, String email) {
        User user = User.builder()
                .id(userId)
                .username(firstName + "-" + lastName)
                .email(email)
                .build();
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(SELECT_USER_BY_NAME, User.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("name", user.getUsername())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(user));

        Optional<User> result = userDao.getByName(user.getUsername());

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(entityManager).createQuery(SELECT_USER_BY_NAME, User.class);
        verify(typedQuery).setParameter("name", user.getUsername());
        verify(typedQuery).getResultList();
        verify(entityManager).close();
    }

    @Test
    @DisplayName("Test get user by name when no user found")
    void testGetByNameNoUserFound() {
        String name = "User";

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(SELECT_USER_BY_NAME, User.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("name", name)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());

        Optional<User> result = userDao.getByName(name);

        assertFalse(result.isPresent());
        verify(entityManager).createQuery(SELECT_USER_BY_NAME, User.class);
        verify(typedQuery).setParameter("name", name);
        verify(typedQuery).getResultList();
        verify(entityManager).close();
    }

    @Test
    void testSaveUser() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.createQuery("SELECT u FROM User u WHERE u.username = :name", User.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("name", user.getUsername())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());

        User savedUser = userDao.save(user);

        assertEquals(user, savedUser);
        verify(entityManager).createQuery("SELECT u FROM User u WHERE u.username = :name", User.class);
        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(typedQuery).setParameter("name", user.getUsername());
        verify(typedQuery).getResultList();
        verify(entityManager).persist(user);
        verify(transaction).commit();
    }

    @Test
    @DisplayName("Test save method with PersistenceException")
    void testSaveWithUserAlreadyExistsException() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.createQuery(SELECT_USER_BY_NAME, User.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("name", user.getUsername())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(user));
        when(transaction.isActive()).thenReturn(true);

        assertThrows(PersistenceException.class, () -> userDao.save(user));
        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(entityManager).createQuery(SELECT_USER_BY_NAME, User.class);
        verify(typedQuery).setParameter("name", user.getUsername());
        verify(typedQuery).getResultList();
        verify(transaction).rollback();
        verify(entityManager).close();
    }
}
