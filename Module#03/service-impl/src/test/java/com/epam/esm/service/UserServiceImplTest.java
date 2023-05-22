package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        pageable = PageRequest.of(0, 25, Sort.by("name").ascending());
    }

    @ParameterizedTest
    @DisplayName("Get all users")
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
        List<UserDto> expectedDtos = Arrays.asList(
                UserDto.builder().id(id1).username(username1).email(email1).build(),
                UserDto.builder().id(id1 + 1).username(username2).email(email2).build()
        );

        when(userDao.getAll(pageable)).thenReturn(users);
        when(mapper.toDtoList(users)).thenReturn(expectedDtos);

        assertEquals(expectedDtos, userService.getAll(pageable).getContent());

        verify(userDao).getAll(pageable);
        verify(mapper).toDtoList(users);
        verifyNoMoreInteractions(userDao, mapper);
    }

    @ParameterizedTest
    @DisplayName("Find user by ID")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com",
            "2, Emma, Liam, Emma-Liam@gmail.com",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com",
            "5, Mia, Aiden, Mia-Aiden@gmail.com"
    })
    void findByIdTest(Long id, String firstName, String lastName, String email) {

        User expectedUser = User.builder().id(id).username(firstName + "-" + lastName).email(email).build();
        Optional<User> optionalUser = Optional.of(expectedUser);

        when(userDao.getById(id)).thenReturn(optionalUser);

        assertEquals(expectedUser, userService.findById(id));

        verify(userDao).getById(id);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    @DisplayName("Find user by ID - User not found")
    void findByIdNotFoundTest() {
        Long id = 1L;
        Optional<User> optionalUser = Optional.empty();

        when(userDao.getById(id)).thenReturn(optionalUser);

        assertThrows(UserNotFoundException.class, () -> userService.findById(id));

        verify(userDao).getById(id);
        verifyNoMoreInteractions(userDao);
    }

    @ParameterizedTest
    @DisplayName("Find user by ID With Order")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com, 10, Java, description, 10, 30",
            "2, Emma, Liam, Emma-Liam@gmail.com, 20, Certificate, description, 20, 45",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com, 30, Spring, description, 30, 60",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com, 40, SQL, description, 40, 75",
            "5, Ava, Leo, Ava-Leo@gmail.com, 50, Programming, description, 50, 90"
    })
    void testFindByIdWithOrder(Long userId, String firstName, String lastName, String email,
                               long certificateId, String name, String description,
                               BigDecimal price, int duration) {

        Certificate certificate = Certificate.builder().id(certificateId).name(name).description(description).duration(duration).build();
        Order order = Order.builder().id(certificateId).cost(price).certificates(Collections.singleton(certificate)).build();
        User expectedUser = User.builder().id(userId).username(firstName + "-" + lastName).orders(Collections.singleton(order)).email(email).build();
        Optional<User> optionalUser = Optional.of(expectedUser);

        when(userDao.getById(userId)).thenReturn(optionalUser);

        assertEquals(expectedUser, userService.findById(userId));

        verify(userDao).getById(userId);
        verifyNoMoreInteractions(userDao);
    }

    @ParameterizedTest
    @DisplayName("Get user by ID With Order")
    @CsvSource({
            "1, Olivia, Noah, Olivia-Noah@gmail.com, 10, Java, description, 10, 30",
            "2, Emma, Liam, Emma-Liam@gmail.com, 20, Certificate, description, 20, 45",
            "3, Charlotte, Oliver, Charlotte-Oliver@gmail.com, 30, Spring, description, 30, 60",
            "4, Amelia, Elijah, Amelia-Elijah@gmail.com, 40, SQL, description, 40, 75",
            "5, Ava, Leo, Ava-Leo@gmail.com, 50, Programming, description, 50, 90"
    })
    void getByIdWithOrderTest(Long userId, String firstName, String lastName, String email,
                              long certificateId, String name, String description,
                              BigDecimal price, int duration) {

        Certificate certificate = Certificate.builder().id(certificateId).name(name).description(description).duration(duration).build();
        Order order = Order.builder().id(certificateId).cost(price).certificates(Collections.singleton(certificate)).build();
        User expectedUser = User.builder().id(userId).username(firstName + "-" + lastName).orders(Collections.singleton(order)).email(email).build();

        when(userDao.getById(userId)).thenReturn(Optional.of(expectedUser));
        when(mapper.toDto(expectedUser)).thenReturn(UserDto.builder().id(userId).username(firstName + lastName).email(email).build());

        UserDto actualUserDto = userService.getById(userId);

        assertEquals(expectedUser.getId(), actualUserDto.getId());
        assertEquals(expectedUser.getEmail(), actualUserDto.getEmail());

        verify(userDao).getById(userId);
        verify(mapper).toDto(expectedUser);
        verifyNoMoreInteractions(userDao, mapper);
    }
}
