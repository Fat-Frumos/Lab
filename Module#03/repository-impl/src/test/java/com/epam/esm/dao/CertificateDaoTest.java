package com.epam.esm.dao;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateDaoTest {
    @Mock
    private EntityManagerFactory entityManagerFactory;
    @Mock
    private EntityTransaction transaction;
    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<Tag> tagTypedQuery;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private CriteriaQuery<Certificate> criteriaQuery;
    @Mock
    private TypedQuery<Certificate> typedQuery;
    private CertificateDao certificateDao;
    private final Criteria criteria = Criteria.builder().page(0).size(25).build();
    private Tag tag1;
    private Tag tag2;


    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        criteriaBuilder = mock(CriteriaBuilder.class);
        criteriaQuery = mock(CriteriaQuery.class);
        typedQuery = mock(TypedQuery.class);
        entityManager = mock(EntityManager.class);
        transaction = mock(EntityTransaction.class);
        tagTypedQuery = mock(TypedQuery.class);

        certificateDao = new CertificateDaoImpl(entityManagerFactory);

        tag1 = Tag.builder().build();
        tag2 = Tag.builder().build();
    }

    @DisplayName("Test find certificate by ID")
    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void testFindById(Long id, String name, String description, BigDecimal price, int duration) {

        Certificate certificate = Certificate.builder().id(id).name(name)
                .description(description).price(price)
                .tags(new HashSet<>(Arrays.asList(tag1, tag2)))
                .duration(duration).build();

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.find(Certificate.class, id)).thenReturn(certificate);

        Certificate actual = certificateDao.findById(id);

        assertNotNull(actual.getId());
        assertEquals(id, actual.getId());
        assertEquals(name, actual.getName());
        assertEquals(description, actual.getDescription());
        assertEquals(price, actual.getPrice());
        assertEquals(duration, actual.getDuration());
        assertEquals(certificate, actual);
        verify(entityManager).find(Certificate.class, id);
    }

    @DisplayName("Test getAll method")
    @ParameterizedTest
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void testGetAll(Long id, String name, String description, BigDecimal price, Integer duration) {
        Certificate certificate = Certificate
                .builder().id(id).name(name)
                .description(description).price(price)
                .duration(duration).build();

        List<Certificate> expected = Collections.singletonList(certificate);

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Certificate.class)).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(criteria.getPage() * criteria.getSize())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(criteria.getSize())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expected);

        List<Certificate> actual = certificateDao.getAll(criteria);

        assertEquals(expected, actual);
        verify(entityManagerFactory).createEntityManager();
        verify(entityManager).getCriteriaBuilder();
        verify(criteriaBuilder).createQuery(Certificate.class);
        verify(criteriaQuery).from(Certificate.class);
        verify(entityManager).createQuery(criteriaQuery);
        verify(typedQuery).setFirstResult(criteria.getPage() * criteria.getSize());
        verify(typedQuery).setMaxResults(criteria.getSize());
        verify(typedQuery).getResultList();
    }

    @DisplayName("Save certificate")
    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void testSave(Long id, String name, String description, BigDecimal price, int duration) {

        Certificate certificate = Certificate.builder()
                .id(id).name(name)
                .description(description).price(price)
                .duration(duration).build();

        Tag tag = Tag.builder().name("Seasonal").build();
        Set<Tag> tags = new HashSet<>();
        tags.add(tag);
        certificate.setTags(tags);
        String sql = "SELECT t FROM Tag t WHERE t.name = :name";
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.createQuery(sql, Tag.class)).thenReturn(tagTypedQuery);
        when(tagTypedQuery.setParameter("name", tag.getName())).thenReturn(tagTypedQuery);
        when(tagTypedQuery.getResultList()).thenReturn(Collections.singletonList(tag));
        when(entityManager.merge(certificate)).thenReturn(certificate);

        Certificate actualCertificate = certificateDao.save(certificate);

        assertEquals(certificate, actualCertificate);
        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(entityManager).createQuery(sql, Tag.class);
        verify(tagTypedQuery).setParameter("name", tag.getName());
        verify(tagTypedQuery).getResultList();
        verify(entityManager).merge(certificate);
        verify(transaction).commit();
    }
}
