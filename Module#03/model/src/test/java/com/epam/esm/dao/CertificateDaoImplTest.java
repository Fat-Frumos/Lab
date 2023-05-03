package com.epam.esm.dao;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.FilterParams;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.sqm.SortOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateDaoImplTest {
    @Mock
    private EntityManager entityManager;
    @Mock
    private CriteriaBuilder builder;
    @Mock
    private CriteriaQuery<Certificate> criteriaQuery;
    @Mock
    private Root<Certificate> root;
    @Mock
    private Join<Certificate, Tag> join;
    @Mock
    private TypedQuery<Certificate> certificateQuery;
    private CertificateDao certificateDao;
    private Certificate certificate1;
    private Tag spring;
    private Tag summer;
    private Certificate certificate2;
    private List<Certificate> expected;

    private Criteria criteria;

    @BeforeEach
    void setUp() {

        spring = Tag.builder().name("spring").build();
        summer = Tag.builder().name("summer").build();

        criteria = Criteria.builder()
                .offset(0L)
                .page(0L)
                .size(25L)
                .sortOrder(SortOrder.ASCENDING)
                .filterParams(FilterParams.ID)
                .build();

        certificate1 = Certificate.builder()
                .name("Certificate A")
                .description("Description 1")
                .price(new BigDecimal("10.99"))
                .createDate(Timestamp.valueOf(LocalDateTime.now()))
                .lastUpdateDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        certificate2 = Certificate.builder()
                .name("Certificate B")
                .description("Description 2")
                .price(new BigDecimal("20.99"))
                .createDate(Timestamp.valueOf(LocalDateTime.now()))
                .lastUpdateDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        expected = Arrays.asList(certificate1, certificate2);

        certificateDao = new CertificateDaoImpl(entityManager);
        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createQuery(Certificate.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Certificate.class)).thenReturn(root);
        lenient().when(root.join(anyString(), any())).thenAnswer(
                invocation -> root.join(String.valueOf(
                        invocation.getArgument(0)), JoinType.INNER));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(certificateQuery);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    @DisplayName("Test getAllBy with criteria")
    void testGetAllByWithParams(Long id, String name) {

        criteria.addParam(id);
        criteria.addParam(name);

        certificate1.setTags(new HashSet<>(Arrays.asList(spring, summer)));
        certificate2.addTag(spring);

        when(certificateQuery.getResultList()).thenReturn(expected);

        List<Certificate> actual = certificateDao.getAllBy(criteria);
        assertEquals(2, actual.size());
        assertEquals(certificate1.getName(), actual.get(0).getName());

        List<Certificate> expected = Arrays.asList(certificate1, certificate2);

        assertAll("Verify getAllBy with criteria",
                () -> assertEquals(expected.size(), actual.size()),
                () -> assertTrue(expected.containsAll(actual))
        );

        verify(root, times(1)).get(anyString());
        verify(certificateQuery, times(1)).getResultList();
    }

    @ParameterizedTest
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    void testGetAllByCriteria(Long id, String name) {
        criteria.addParam(id);
        criteria.addParam(name);

        when(builder.createQuery(Certificate.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Certificate.class)).thenReturn(root);
        lenient().when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(certificateQuery.getResultList()).thenReturn(expected);

        List<Certificate> actual = certificateDao.getAllBy(criteria);

        assertEquals(2, actual.size());
        assertEquals(certificate1.getName(), actual.get(0).getName());

        assertAll("Verify getAllBy with criteria",
                () -> assertEquals(expected.size(), actual.size()),
                () -> assertTrue(expected.containsAll(actual))
        );

        verify(root, times(1)).get(anyString());
        verify(certificateQuery, times(1)).getResultList();
    }

    @Test
    @DisplayName("Test and Verify getAllBy with criteria")
    void testGetAllByWithDefaultCriteria() {
        certificate1.setTags(new HashSet<>(Arrays.asList(spring, summer)));
        certificate2.addTag(spring);
        when(certificateQuery.getResultList()).thenReturn(expected);

        List<Certificate> actual = certificateDao.getAllBy(criteria);
        assertEquals(2, actual.size());
        assertEquals(certificate1.getName(), actual.get(0).getName());

        List<Certificate> expected = Arrays.asList(certificate1, certificate2);

        assertAll("Verify getAllBy with criteria",
                () -> assertEquals(expected.size(), actual.size()),
                () -> assertTrue(expected.containsAll(actual))
        );

        verify(root, times(1)).get(anyString());
        verify(certificateQuery, times(1)).getResultList();
    }

    @Test
    @DisplayName("Test and Verify getAllBy with tags")
    void testGetAllByTags() {
        certificate1.setTags(new HashSet<>(Arrays.asList(spring, summer)));
        when(certificateQuery.getResultList()).thenReturn(expected);
        List<Certificate> result = certificateDao.getAllBy(criteria);
        assertEquals(2, result.size());
        assertEquals(certificate1.getName(), result.get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    @DisplayName("Test getAllBy with criteria")
    void testGetAllByWithParam(Long id, String name) {

        criteria.addParam(id);
        criteria.addParam(name);

        certificate1.setTags(new HashSet<>(Arrays.asList(spring, summer)));
        certificate2.addTag(spring);

        Predicate predicate = builder.equal(root.get("name"), name);

        when(builder.createQuery(Certificate.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Certificate.class)).thenReturn(root);

        List<Tag> tags = Arrays.asList(spring, summer);
        lenient().when(join.in(tags)).thenReturn(predicate);
        criteria.addParam(tags);

        when(certificateQuery.getResultList()).thenReturn(expected);

        List<Certificate> actual = certificateDao.getAllBy(criteria);

        assertEquals(2, actual.size());
        assertEquals(certificate1.getName(), actual.get(0).getName());

        assertAll("Verify getAllBy with criteria",
                () -> assertEquals(expected.size(), actual.size()),
                () -> assertTrue(expected.containsAll(actual))
        );

        verify(certificateQuery, times(1)).getResultList();
    }
}
