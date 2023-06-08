package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Subgraph;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.epam.esm.dao.Queries.FETCH_GRAPH;
import static com.epam.esm.dao.Queries.ID;
import static com.epam.esm.dao.Queries.SELECT_ORDER_BY_ID;
import static com.epam.esm.dao.Queries.SELECT_ORDER_BY_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class OrderDaoTest {
    @Mock
    Subgraph<Object> subgraph;
    @Mock
    private Root<Order> root;
    @Mock
    private EntityGraph graph;
    @Mock
    Expression<Number> expression;
    @Mock
    TypedQuery<Tuple> tupleTypedQuery;
    @Mock
    private EntityManagerFactory factory;
    @Mock
    private EntityTransaction transaction;
    @Mock
    private EntityManager entityManager;
    @Mock
    private CriteriaQuery<Tuple> tupleCriteriaQuery;
    @Mock
    private TypedQuery<Order> typedQuery;
    @Mock
    private CriteriaBuilder builder;
    @Mock
    private CriteriaQuery<Order> query;
    @Mock
    private Fetch<Order, Certificate> certificatesFetch;
    @Mock
    private Fetch<Certificate, Tag> tagsFetch;
    @Mock
    private Fetch<Order, User> userFetch;
    @Mock
    private Tuple tuple;
    @Mock
    private Join<Object, Object> tagJoin;
    @Mock
    private Path<Object> userPath;
    @Mock
    private Path<Object> idPath;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDaoImpl(factory);
    }

    @Test
    void testGetById() {
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(SELECT_ORDER_BY_ID, Order.class)).thenReturn(this.typedQuery);
        when(typedQuery.setParameter(ID, id)).thenReturn(this.typedQuery);
        when(typedQuery.setHint(FETCH_GRAPH, graph)).thenReturn(this.typedQuery);
        when(typedQuery.getResultStream()).thenReturn(Stream.of(order));
        when(typedQuery.setParameter(ID, id)).thenReturn(typedQuery);
        when(entityManager.getEntityGraph("Order.certificates.tags")).thenReturn(graph);
        when(typedQuery.setHint(FETCH_GRAPH, graph)).thenReturn(typedQuery);
        when(typedQuery.getResultStream()).thenReturn(Stream.of(order));

        Optional<Order> result = orderDao.getById(id);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());
        verify(factory).createEntityManager();
        verify(entityManager).createQuery(SELECT_ORDER_BY_ID, Order.class);
        verify(typedQuery).setParameter(ID, id);
        verify(typedQuery).setHint(FETCH_GRAPH, graph);
        verify(typedQuery).getResultStream();
        verify(entityManager).getEntityGraph("Order.certificates.tags");
        verify(typedQuery, times(1)).setParameter(ID, id);
        verify(typedQuery, times(1)).setHint(FETCH_GRAPH, graph);
        verify(typedQuery, times(1)).getResultStream();
    }

    @Test
    void testGetUserOrders() {
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createQuery(Order.class)).thenReturn(query);
        when(query.from(Order.class)).thenReturn(root);
        when(entityManager.createEntityGraph(Order.class)).thenReturn(graph);
        when(graph.addSubgraph("certificate")).thenReturn(subgraph);
        when(query.where(builder.equal(root.get("user"), user))).thenReturn(query);
        when(entityManager.createQuery(query)).thenReturn(typedQuery);
        when(typedQuery.setHint(FETCH_GRAPH, graph)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(pageable.getPageSize())).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedOrders);

        List<Order> result = orderDao.getUserOrders(user, pageable);

        assertEquals(expectedOrders, result);
        verify(factory).createEntityManager();
        verify(entityManager).getCriteriaBuilder();
        verify(builder).createQuery(Order.class);
        verify(query).from(Order.class);
        verify(entityManager).createEntityGraph(Order.class);
        verify(graph).addSubgraph("certificate");
        verify(query).where(builder.equal(root.get("user"), user));
        verify(entityManager).createQuery(query);
        verify(typedQuery).setHint(FETCH_GRAPH, graph);
        verify(typedQuery).setMaxResults(pageable.getPageSize());
        verify(typedQuery).setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        verify(typedQuery).getResultList();
    }

    @Test
    void testGetByName() {
        String username = "Pasha";
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(SELECT_ORDER_BY_NAME, Order.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(order));
        Optional<Order> result = orderDao.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }

//    @Test
//    void testFindOrdersByUserId() {
//        when(entityManager.createEntityGraph(Order.class)).thenReturn(graph);
//        when(factory.createEntityManager()).thenReturn(entityManager);
//        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
//        when(builder.createQuery(Order.class)).thenReturn(query);
//        when(query.from(Order.class)).thenReturn(root);
//        when(root.get(USER)).thenReturn(userPath);
//        when(query.select(root)).thenReturn(query);
//        when(userPath.get(ID)).thenReturn(idPath);
//        when(graph.addSubgraph(CERTIFICATES)).thenReturn(subgraph);
//        when(typedQuery.setHint(FETCH_GRAPH, graph)).thenReturn(typedQuery);
//        when(typedQuery.getResultList()).thenReturn(expectedOrders);
//        when(entityManager.createQuery(query)).thenReturn(typedQuery);
//
//        List<Order> result = orderDao.findOrdersByUserId(id);
//
//        assertEquals(expectedOrders, result);
//        verify(entityManager).createEntityGraph(Order.class);
//        verify(entityManager).getCriteriaBuilder();
//        verify(builder).createQuery(Order.class);
//        verify(query).from(Order.class);
//        verify(root).get(USER);
//        verify(userPath).get(ID);
//        verify(graph).addSubgraph(CERTIFICATES);
//        verify(typedQuery).setHint(FETCH_GRAPH, graph);
//        verify(typedQuery).getResultList();
//        verify(entityManager).createQuery(query);
//    }

//    @Test
//    void testGetMostUsedTag() {
//        Join<Object, Object> userJoin = mock(Join.class);
//        Join<Object, Object> certificateJoin = mock(Join.class);
//        Tuple tuple = mock(Tuple.class);
//
//        when(factory.createEntityManager()).thenReturn(entityManager);
//        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
//        when(builder.createTupleQuery()).thenReturn(tupleCriteriaQuery);
//        when(tupleCriteriaQuery.from(Order.class)).thenReturn(root);
//        when(root.join("user")).thenReturn(userJoin);
//        when(root.join("certificates")).thenReturn(certificateJoin);
//        when(certificateJoin.join("tags")).thenReturn(tagJoin);
//        when(builder.sum(root.get("cost"))).thenReturn(expression);
//        when(tagJoin.alias("tag")).thenReturn(tagJoin);
//        when(expression.alias("totalCost")).thenReturn(expression);
//        when(tupleCriteriaQuery.multiselect(tagJoin.alias("tag"), expression.alias("totalCost"))).thenReturn(tupleCriteriaQuery);
//        when(tupleCriteriaQuery.groupBy(tagJoin)).thenReturn(tupleCriteriaQuery);
//        when(tupleCriteriaQuery.orderBy(builder.desc(builder.sum(root.get("cost"))))).thenReturn(tupleCriteriaQuery);
//        when(entityManager.createQuery(tupleCriteriaQuery)).thenReturn(tupleTypedQuery);
//        when(tupleTypedQuery.getResultList()).thenReturn(Collections.singletonList(tuple));
//        when(tupleTypedQuery.setMaxResults(1)).thenReturn(tupleTypedQuery);
//        when(tuple.get("tag", Tag.class)).thenReturn(expectedTag);
//
//        Optional<Tag> result = orderDao.getMostUsedTagBy(id);
//
//        assertTrue(result.isPresent());
//        assertEquals(expectedTag, result.get());
//
//        verify(factory).createEntityManager();
//        verify(entityManager).getCriteriaBuilder();
//        verify(builder).createTupleQuery();
//        verify(tupleCriteriaQuery).from(Order.class);
//        verify(root).join("user");
//        verify(root).join("certificates");
//        verify(certificateJoin).join("tags");
//        verify(tupleCriteriaQuery).multiselect(tagJoin.alias("tag"), expression.alias("totalCost"));
//        verify(tupleCriteriaQuery).groupBy(tagJoin);
//        verify(tupleCriteriaQuery).orderBy(builder.desc(builder.sum(root.get("cost"))));
//        verify(entityManager).createQuery(tupleCriteriaQuery);
//        verify(tupleTypedQuery).setMaxResults(1);
//        verify(tupleTypedQuery).getResultList();
//    }
//
//    @Test
//    void testGetUserOrder() {
//        when(root.get(USER)).thenReturn(userPath);
//        when(userPath.get(ID)).thenReturn(idPath);
//        when(factory.createEntityManager()).thenReturn(entityManager);
//        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
//        when(builder.createQuery(Order.class)).thenReturn(query);
//        when(query.from(Order.class)).thenReturn(root);
//        when(query.select(root)).thenReturn(query);
//        when(entityManager.createEntityGraph(Order.class)).thenReturn(graph);
//        when(entityManager.createQuery(query)).thenReturn(typedQuery);
//        when(typedQuery.setHint(FETCH_GRAPH, graph)).thenReturn(typedQuery);
//        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(order));
//
//        Optional<Order> result = orderDao.getUserOrder(id2, id);
//
//        assertEquals(order, result.get());
//        verify(root).get("user");
//        verify(userPath).get("id");
//        verify(factory).createEntityManager();
//        verify(entityManager).getCriteriaBuilder();
//        verify(builder).createQuery(Order.class);
//        verify(query).from(Order.class);
//        verify(query).select(root);
//        verify(entityManager).createQuery(query);
//        verify(typedQuery).getResultList();
//    }

    @Test
    @DisplayName("Test save order")
    void testSave() {
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.find(User.class, user.getId())).thenReturn(user);
        when(entityManager.find(Certificate.class, certificate.getId())).thenReturn(certificate);
        Order savedOrder = orderDao.save(order);
        assertEquals(order, savedOrder);
        verify(transaction).begin();
        verify(transaction).commit();
    }

    @Test
    @DisplayName("Test save order and Rollback")
    void testSaveRollback() {
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.getReference(User.class, user.getId()))
                .thenThrow(new IllegalArgumentException());
        when(transaction.isActive()).thenReturn(true);
        assertThrows(PersistenceException.class, () -> orderDao.save(order));
        verify(transaction).begin();
        verify(transaction).rollback();
    }

//    @Test
//    void testGetAllBy() {
//        when(factory.createEntityManager()).thenReturn(entityManager);
//        when(builder.createQuery(Order.class)).thenReturn(query);
//        when(query.select(root)).thenReturn(query);
//        when(query.from(Order.class)).thenReturn(root);
//        when(typedQuery.setHint(anyString(), any())).thenReturn(typedQuery);
//        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
//        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
//        when(entityManager.createQuery(query)).thenReturn(typedQuery);
//        when(builder.createQuery(Order.class)).thenReturn(query);
//        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
//        doReturn(certificatesFetch).when(root).fetch("certificates", JoinType.LEFT);
//        doReturn(tagsFetch).when(certificatesFetch).fetch("tags", JoinType.LEFT);
//        doReturn(userFetch).when(root).fetch("user", JoinType.LEFT);
//
//        OrderDao orderDao = new OrderDaoImpl(factory);
//        Pageable pageable = PageRequest.of(0, 10);
//
//        when(typedQuery.getResultList()).thenReturn(expectedOrders);
//        List<Order> result = orderDao.getAllBy(pageable);
//        assertEquals(expectedOrders, result);
//        assertNotNull(result);
//        verify(factory).createEntityManager();
//        verify(entityManager).getCriteriaBuilder();
//        verify(builder).createQuery(Order.class);
//        verify(query).from(Order.class);
//        verify(query).select(root);
//        verify(entityManager).createQuery(query);
//        verify(typedQuery).setFirstResult(0);
//        verify(typedQuery).setMaxResults(10);
//        verify(typedQuery).getResultList();
//    }

    private final Long id = 1L;
    private final Tag expectedTag = Tag.builder().id(id).build();
    private final Long id2 = 2L;
    private final User user = User.builder().id(id).username("Olivia-Noah").email("Olivia-Noah@gmail.com").build();
    private final User user2 = User.builder().id(2L).username("Emma-Liam").email("Emma-Liam@gmail.com").build();
    private final Certificate certificate = Certificate.builder().id(id).name("Java").description("Core").price(BigDecimal.valueOf(100)).duration(50).build();
    private final Certificate certificate2 = Certificate.builder().id(id2).name("Spring").description("Boot").price(BigDecimal.valueOf(20)).duration(45).build();
    private final Pageable pageable = PageRequest.of(0, 25, Sort.by("name").ascending());
    private final Order order = Order.builder().id(1L).user(user).certificates(Collections.singleton(certificate)).build();
    private final Order order2 = Order.builder().id(2L).user(user2).certificates(Collections.singleton(certificate2)).build();
    private final List<Order> expectedOrders = Arrays.asList(order, order2);
}
