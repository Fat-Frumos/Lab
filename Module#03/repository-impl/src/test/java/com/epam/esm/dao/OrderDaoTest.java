package com.epam.esm.dao;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PostCertificateDto;
import com.epam.esm.dto.UserSlimDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class OrderDaoTest {
    @Mock
    private Root<Order> root;
    @Mock
    private EntityGraph<?> entityGraph;
    @Mock
    private EntityManagerFactory entityManagerFactory;
    @Mock
    private EntityTransaction transaction;
    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<Order> typedQuery;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private CriteriaQuery<Order> criteriaQuery;
    OrderDaoImpl dao;
    private final Long id = 1L;
    private final Long id2 = 2L;
    private final User user = User.builder()
            .id(id)
            .username("Olivia-Noah")
            .email("Olivia-Noah@gmail.com")
            .build();
    private final User user2 = User.builder()
            .id(2L)
            .username("Emma-Liam")
            .email("Emma-Liam@gmail.com")
            .build();

    private final UserSlimDto userDto2 = UserSlimDto.builder()
            .id(2L)
            .username("Emma-Liam")
            .email("Emma-Liam@gmail.com")
            .build();
    private final UserSlimDto userDto = UserSlimDto.builder()
            .id(id)
            .username("Olivia-Noah")
            .email("Olivia-Noah@gmail.com")
            .build();
    private final Certificate certificate = Certificate.builder()
            .id(id)
            .name("Java")
            .description("Core")
            .price(BigDecimal.valueOf(100))
            .duration(50)
            .build();
    private final PostCertificateDto certificateDto = PostCertificateDto.builder()
            .id(id)
            .name("Java")
            .description("Core")
            .price(BigDecimal.valueOf(100))
            .duration(50)
            .build();

    private final Certificate certificate2 = Certificate.builder()
            .id(id2)
            .name("Spring")
            .description("Boot")
            .price(BigDecimal.valueOf(20))
            .duration(45)
            .build();

    private final PostCertificateDto certificateDto2 = PostCertificateDto.builder()
            .id(id2)
            .name("Spring")
            .description("Boot")
            .price(BigDecimal.valueOf(20))
            .duration(45)
            .build();
    private final Pageable pageable = PageRequest.of(0, 25, Sort.by("name").ascending());
    private final Order order = Order.builder().id(1L).user(user).certificates(Collections.singleton(certificate)).build();
    private final Order order2 = Order.builder().id(2L).user(user2).certificates(Collections.singleton(certificate2)).build();
    private final OrderDto orderDto = OrderDto.builder().id(id).user(userDto).certificateDtos(Collections.singleton(certificateDto)).build();
    private final OrderDto orderDto2 = OrderDto.builder().id(id2).user(userDto2).certificateDtos(Collections.singleton(certificateDto2)).build();
    private final List<OrderDto> orderDtos = Arrays.asList(orderDto, orderDto2);
    private final List<Order> orders = Arrays.asList(order, order2);

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        criteriaBuilder = mock(CriteriaBuilder.class);
        criteriaQuery = mock(CriteriaQuery.class);
        typedQuery = mock(TypedQuery.class);
        entityManager = mock(EntityManager.class);
        transaction = mock(EntityTransaction.class);
        dao = new OrderDaoImpl(entityManagerFactory);
    }

    @Test
    void testGetOrders() {

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Order.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Order.class)).thenReturn(root);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.setHint(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        List<Order> expectedOrders = Arrays.asList(order, order2);
        when(typedQuery.getResultList()).thenReturn(expectedOrders);

        List<Order> orders = dao.getAllBy(pageable);

        assertNotNull(orders);

        verify(entityManager).createQuery(criteriaQuery);
        verify(typedQuery).setHint(anyString(), any());
        verify(typedQuery).setFirstResult(anyInt());
        verify(typedQuery).setMaxResults(anyInt());
        verify(typedQuery).getResultList();
        verify(entityManager, times(1)).createQuery(criteriaQuery);
    }
}
