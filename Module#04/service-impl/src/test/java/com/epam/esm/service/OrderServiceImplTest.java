package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PostCertificateDto;
import com.epam.esm.dto.UserSlimDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.exception.UnauthorizedAccessException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private UserDao userDao;
    @Mock
    private CertificateDao certificateDao;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderServiceImpl orderService;
    private final Long orderId = 1L;
    private final Long userId = 1L;
    Set<Long> certificateIds = new HashSet<>();
    private final Long id = 1L;
    private final Long id2 = 2L;
    private final User user = User.builder()
            .id(userId)
            .username("Olivia-Noah")
            .email("Olivia-Noah@gmail.com")
            .build();
    private final User user2 = User.builder()
            .id(2L)
            .username("Emma-Liam")
            .email("Emma-Liam@gmail.com")
            .build();

    private final UserSlimDto userDto2 = getUserSlimDto(2L, "Emma-Liam@gmail.com", "Emma-Liam");
    private final UserSlimDto userDto = getUserSlimDto(userId, "Olivia-Noah@gmail.com", "Olivia-Noah");
    private final Certificate certificate = getCertificate(id, "Java", "Core", BigDecimal.valueOf(100), 50);
    private final PostCertificateDto certificateDto = getPostCertificateDto(id, "Java", "Core", BigDecimal.valueOf(100), 50);
    private final Certificate certificate2 = getCertificate(id2, "Spring", "Boot", BigDecimal.valueOf(20), 45);
    private final PostCertificateDto certificateDto2 = getPostCertificateDto(id2, "Spring", "Boot", BigDecimal.valueOf(20), 45);
    private final Order order = Order.builder().id(1L).user(user)
            .certificates(Collections.singleton(certificate)).build();
    private final Order order2 = Order.builder().id(2L).user(user2)
            .certificates(Collections.singleton(certificate2)).build();
    private final OrderDto orderDto = OrderDto.builder().id(id).user(userDto)
            .certificateDtos(Collections.singleton(certificateDto)).build();
    private final OrderDto expectedOrderDto = OrderDto.builder().id(id2).user(userDto2)
            .certificateDtos(Collections.singleton(certificateDto2)).build();
    private final List<OrderDto> orderDtos = Arrays.asList(orderDto, expectedOrderDto);
    private final List<Order> orders = Arrays.asList(order, order2);
    private final Pageable pageable = PageRequest.of(0, 25, Sort.by("name").ascending());

    @BeforeEach
    public void setUp() {
        certificateIds.add(10L);
        certificateIds.add(20L);
        certificateIds.add(30L);
    }

    @Test
    @DisplayName("Given a valid order ID, when getOrderById is called, then return the corresponding order")
    void testGetById() {
        when(orderDao.getById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        OrderDto actualOrderDto = orderService.getById(orderId);
        assertThat(actualOrderDto).usingRecursiveComparison().isEqualTo(orderDto);
        assertEquals(orderDto.getCertificateDtos().size(), actualOrderDto.getCertificateDtos().size());
        assertEquals(orderDto.getCertificateDtos().iterator().next().getId(),
                actualOrderDto.getCertificateDtos().iterator().next().getId());
        verify(orderDao).getById(orderId);
        verify(orderMapper).toDto(order);
        verifyNoMoreInteractions(orderDao, orderMapper);
    }

    @Test
    @DisplayName("Get Order by ID - Order Not Found")
    void getByIdOrderNotFoundTest() {
        when(orderDao.getById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.getById(orderId));
        verify(orderDao).getById(orderId);
        verifyNoMoreInteractions(orderDao, orderMapper);
    }

    @Test
    @DisplayName("Create Order")
    void createOrderTest() {
        Set<Long> certificateIds = Collections.singleton(id);
        List<Certificate> certificates = Collections.singletonList(certificate);
        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(certificateDao.findAllByIds(certificateIds)).thenReturn(certificates);
        when(orderDao.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        OrderDto actualOrderDto = orderService.createOrder(user.getUsername(), userId, certificateIds);
        assertThat(actualOrderDto).usingRecursiveComparison().isEqualTo(orderDto);
        assertEquals(orderDto.getCertificateDtos().size(), actualOrderDto.getCertificateDtos().size());
        assertEquals(orderDto.getCertificateDtos().iterator().next().getId(),
                actualOrderDto.getCertificateDtos().iterator().next().getId());
        verify(userDao).findByUsername(user.getUsername());
        verify(certificateDao).findAllByIds(certificateIds);
        verify(orderDao).save(any(Order.class));
        verify(orderMapper).toDto(order);
        verifyNoMoreInteractions(userDao, certificateDao, orderDao, orderMapper);
    }

    @Test
    @DisplayName("Save Order - UserNotFoundException")
    void saveOrderUserNotFoundException() {
        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () ->
                orderService.createOrder(user.getUsername(), userId, certificateIds));
        verify(userDao).findByUsername(user.getUsername());
        verifyNoMoreInteractions(userDao, certificateDao, orderDao, orderMapper);
    }

    @Test
    @DisplayName("Save Order - CertificateNotFoundException")
    void saveOrderCertificateNotFoundExceptionTest() {
        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(certificateDao.findAllByIds(certificateIds)).thenReturn(Collections.emptyList());
        assertThrows(CertificateNotFoundException.class,
                () -> orderService.createOrder(user.getUsername(), userId, certificateIds));
        verify(userDao).findByUsername(user.getUsername());
        verify(certificateDao).findAllByIds(certificateIds);
        verifyNoMoreInteractions(userDao, certificateDao, orderDao, orderMapper);
    }

    @ParameterizedTest
    @DisplayName("Get User Orders")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com, 10, Java, description, 10, 30",
            "2, Emma, Liam, Emma-Liam@gmail.com, 20, Certificate, description, 20, 45",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com, 30, Spring, description, 30, 60",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com, 40, SQL, description, 40, 75",
            "5, Ava, Leo, Ava-Leo@gmail.com, 50, Programming, description, 50, 90"
    })
    void getUserOrdersTest(
            Long orderId, String firstName, String lastName, String email,
            long certificateId, String name, String description, BigDecimal price, int duration) {
        List<Order> expectedOrders = Collections.singletonList(order);
        List<OrderDto> expectedOrderDtos = Collections.singletonList(
                getOrderDto(orderId, firstName, lastName, email, certificateId, name, description, price, duration));
        when(orderDao.getUserOrders(user, pageable)).thenReturn(expectedOrders);
        when(orderMapper.toDtoList(expectedOrders)).thenReturn(expectedOrderDtos);
        Page<OrderDto> actualOrderDtos = orderService.getUserOrders(user, pageable);
        assertEquals(expectedOrderDtos.size(), actualOrderDtos.getContent().size());
        assertEquals(expectedOrderDtos.get(0).getId(), actualOrderDtos.getContent().get(0).getId());
        assertEquals(expectedOrderDtos.get(0).getUser().getEmail(),
                actualOrderDtos.getContent().get(0).getUser().getEmail());
        verify(orderDao).getUserOrders(user, pageable);
        verify(orderMapper).toDtoList(expectedOrders);
        verifyNoMoreInteractions(orderDao, orderMapper);
    }

    @Test
    @DisplayName("Get User Order - OrderNotFoundException")
    void getUserOrderOrderNotFoundExceptionTest() {
        when(orderDao.getUserOrder(user.getId(), orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class,
                () -> orderService.getUserOrder(user.getUsername(), user.getId(), orderId));
        verify(orderDao).getUserOrder(user.getId(), orderId);
        verifyNoMoreInteractions(orderDao, orderMapper);
    }

    @Test
    @DisplayName("Test get user order")
    void getUserOrderTest() {
        when(orderDao.getUserOrder(user.getId(), orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        OrderDto actualOrderDto = orderService.getUserOrder(user.getUsername(), user.getId(), orderId);
        assertEquals(orderDto, actualOrderDto);
        verify(orderDao).getUserOrder(user.getId(), orderId);
        verify(orderMapper).toDto(order);
    }

    @Test
    @DisplayName("Test get user order throws order not found exception")
    void getUserOrderThrowsOrderNotFoundExceptionTest() {
        when(orderDao.getUserOrder(user.getId(), orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.getUserOrder(user.getUsername(), user.getId(), orderId));
        verify(orderDao).getUserOrder(user.getId(), orderId);
    }

//    @Test
//    @DisplayName("Create Order - UnauthorizedAccessException")
//    void createOrderUnauthorizedAccessExceptionTest() {
//        when(userDao.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
//        assertThrows(UnauthorizedAccessException.class,
//                () -> orderService.createOrder(user.getUsername(), userId + 1, certificateIds));
//        verify(userDao).findByUsername(user.getUsername());
//        verifyNoMoreInteractions(userDao, certificateDao, orderDao, orderMapper);
//    }

    @ParameterizedTest
    @DisplayName("Get User Orders")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com, 10, Java, description, 10, 30",
            "2, Emma, Liam, Emma-Liam@gmail.com, 20, Certificate, description, 20, 45",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com, 30, Spring, description, 30, 60",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com, 40, SQL, description, 40, 75",
            "5, Ava, Leo, Ava-Leo@gmail.com, 50, Programming, description, 50, 90"
    })
    void getAll(Long id, String firstName, String lastName, String email, long certificateId,
                String name, String description, BigDecimal price, int duration) {
        List<Order> expectedOrders = Collections.singletonList(order);
        List<OrderDto> expectedOrderDtos = Collections.singletonList(
                getOrderDto(id, firstName, lastName, email, certificateId, name, description, price, duration));
        when(orderDao.getAllBy(pageable)).thenReturn(expectedOrders);
        when(orderMapper.toDtoList(expectedOrders)).thenReturn(expectedOrderDtos);
        Page<OrderDto> result = orderService.getAll(pageable);
        assertNotNull(result);
        assertEquals(expectedOrderDtos, result.getContent());
    }

    private static PostCertificateDto getPostCertificateDto(
            long certificateId, String name, String description, BigDecimal price, int duration) {
        return PostCertificateDto.builder()
                .id(certificateId)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();
    }

    @ParameterizedTest
    @DisplayName("Get User Orders")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com, 10, Java, description, 10, 30",
            "2, Emma, Liam, Emma-Liam@gmail.com, 20, Certificate, description, 20, 45",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com, 30, Spring, description, 30, 60",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com, 40, SQL, description, 40, 75",
            "5, Ava, Leo, Ava-Leo@gmail.com, 50, Programming, description, 50, 90"
    })
    void getAllByUserIdTest(
            Long id, String firstName, String lastName, String email, long certificateId,
            String name, String description, BigDecimal price, int duration) {
        User user = User.builder().username("alice").id(id).build();
        when(userDao.findByUsername("alice")).thenReturn(Optional.of(user));
        List<Order> expectedOrders = Collections.singletonList(order);
        List<OrderDto> expectedOrderDtos = Collections.singletonList(
                getOrderDto(id, firstName, lastName, email, certificateId, name, description, price, duration));
        when(orderDao.findOrdersByUserId(id, pageable)).thenReturn(expectedOrders);
        when(orderMapper.toDtoList(expectedOrders)).thenReturn(expectedOrderDtos);
        List<OrderDto> actualOrderDtos = orderService.getAllByUserId("alice", id, pageable);
        assertEquals(expectedOrderDtos, actualOrderDtos);
        verify(orderDao).findOrdersByUserId(id, pageable);
        verify(orderMapper).toDtoList(expectedOrders);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 2, Java, description, 10, 30",
            "2, 3, 7, Oliver, description, 20, 45",
            "3, 6, 3, Spring, description, 30, 60",
            "4, 7, 6, SQL, description, 40, 75",
            "5, 8, 4, Programming, description, 50, 90"
    })
    @DisplayName("testFindCertificateById")
    void testFindCertificateById(
            long id1, long id2, long certificateId, String name,
            String description, BigDecimal price, int duration) {
        Set<Long> ids = new HashSet<>(Arrays.asList(id1, id2));
        Certificate certificate = getCertificate(certificateId, name, description, price, duration);
        List<Certificate> expectedCertificates = new ArrayList<>();
        expectedCertificates.add(certificate);
        when(certificateDao.findAllByIds(ids)).thenReturn(expectedCertificates);
        List<Certificate> result = orderService.findCertificateById(ids);
        assertEquals(expectedCertificates, result);
        verify(certificateDao).findAllByIds(ids);
    }

    @Test
    @DisplayName("Get All Orders")
    void getAllOrdersTest() {
        when(orderDao.getAllBy(pageable)).thenReturn(orders);
        when(orderMapper.toDtoList(orders)).thenReturn(orderDtos);
        Page<OrderDto> result = orderService.getAll(pageable);
        assertNotNull(result);
        assertEquals(orders.size(), result.getContent().size());
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            OrderDto orderDto = result.getContent().get(i);
            assertEquals(order.getId(), orderDto.getId());
            assertEquals(order.getUser().getUsername(), orderDto.getUser().getUsername());
            assertEquals(order.getUser().getEmail(), orderDto.getUser().getEmail());
            Set<Certificate> certificates = order.getCertificates();
            Set<PostCertificateDto> certificateDtos = orderDto.getCertificateDtos();
            assertEquals(certificates.size(), certificateDtos.size());
            Iterator<Certificate> certificateIterator = certificates.iterator();
            Iterator<PostCertificateDto> certificateDtoIterator = certificateDtos.iterator();
            while (certificateIterator.hasNext() && certificateDtoIterator.hasNext()) {
                Certificate certificate = certificateIterator.next();
                PostCertificateDto certificateDto = certificateDtoIterator.next();
                assertEquals(certificate.getId(), certificateDto.getId());
                assertEquals(certificate.getName(), certificateDto.getName());
                assertEquals(certificate.getDescription(), certificateDto.getDescription());
                assertEquals(certificate.getPrice(), certificateDto.getPrice());
                assertEquals(certificate.getDuration(), certificateDto.getDuration());
            }
        }
        verify(orderDao).getAllBy(pageable);
        verifyNoMoreInteractions(orderDao, orderMapper);
    }

    @Test
    @DisplayName("Get Most Used Tags")
    void getMostUsedTagsTest() {
        Tag tag = Tag.builder()
                .id(id)
                .name("Spring")
                .build();
        when(orderDao.getMostUsedTagBy(userId)).thenReturn(Optional.of(tag));
        Tag result = orderService.getMostUsedTags(userId)
                .orElseThrow(() -> new TagNotFoundException(
                        "Tag not found Exception"));
        assertNotNull(result);
        assertEquals(tag.getId(), result.getId());
        assertEquals(tag.getName(), result.getName());
        verify(orderDao).getMostUsedTagBy(userId);
        verifyNoMoreInteractions(orderDao);
    }

    @Test
    void testUpdate() {
        when(orderMapper.toEntity(orderDto)).thenReturn(order);
        when(orderDao.update(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(expectedOrderDto);
        OrderDto actualOrderDto = orderService.update(orderDto);
        assertNotNull(actualOrderDto);
    }

    @Test
    void testDelete() {
        doNothing().when(orderDao).delete(id);
        orderService.delete(id);
        verify(orderDao).delete(id);
    }

    private static OrderDto getOrderDto(
            Long id, String firstName, String lastName, String email, long certificateId,
            String name, String description, BigDecimal price, int duration) {
        return OrderDto.builder()
                .id(id)
                .user(getUserSlimDto(firstName, lastName, email))
                .certificateDtos(Collections.singleton(
                        getPostCertificateDto(certificateId, name, description, price, duration)))
                .build();
    }

    private static UserSlimDto getUserSlimDto(
            Long id, String email, String username) {
        return UserSlimDto.builder()
                .id(id)
                .username(username)
                .email(email)
                .build();
    }

    private static Certificate getCertificate(
            long certificateId, String name, String description,
            BigDecimal price, int duration) {
        return Certificate.builder()
                .id(certificateId)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();
    }

    private static UserSlimDto getUserSlimDto(
            String firstName, String lastName, String email) {
        return UserSlimDto.builder()
                .username(firstName + "-" + lastName)
                .email(email)
                .build();
    }
}
