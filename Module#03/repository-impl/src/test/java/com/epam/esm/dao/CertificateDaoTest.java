package com.epam.esm.dao;

import com.epam.esm.criteria.CertificateQueries;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

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
    private TypedQuery<Tag> typedQuery;

    private CertificateDao certificateDao;

    private List<Certificate> certificates;

    private Tag tag1;
    private Tag tag2;

    @Mock
    Query<Certificate> query;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        entityManager = mock(EntityManager.class);
        transaction = mock(EntityTransaction.class);

        typedQuery = mock(TypedQuery.class);
        query = mock(Query.class);

        Certificate certificate = Certificate.builder().id(1L).name("Winter").description("Season 1").price(BigDecimal.valueOf(10.0)).duration(30).build();

        Certificate certificate2 = Certificate.builder().id(2L).name("Summer").description("Season 2").price(BigDecimal.valueOf(20.0)).duration(45).build();

        certificates = Arrays.asList(certificate, certificate2);

        certificateDao = new CertificateDaoImpl(entityManagerFactory);

        tag1 = Tag.builder().build();
        tag2 = Tag.builder().build();
    }

    @CsvSource({"1, Winter, Season 1, 10.0, 30", "2, Summer, Season 2, 20.0, 45", "3, Spring, Season 3, 30.0, 60", "4, Autumn, Season 4, 40.0, 75"})
    @DisplayName("Test find certificate by ID")
    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    void testFindById(Long id, String name, String description, BigDecimal price, int duration) {

        Certificate certificate = Certificate.builder().id(id).name(name).description(description).price(price).tags(new HashSet<>(Arrays.asList(tag1, tag2))).duration(duration).build();

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


    @Test
    void testGetAll() {

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(CertificateQueries.SELECT_ALL_WITH_TAGS, Certificate.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(certificates);

        List<Certificate> actualCertificates = certificateDao.getAll();

        assertEquals(certificates, actualCertificates);

        verify(entityManagerFactory).createEntityManager();
        verify(entityManager).createQuery(CertificateQueries.SELECT_ALL_WITH_TAGS, Certificate.class);
    }

    @DisplayName("Test getAll method")
    @ParameterizedTest
    @CsvSource({"1, Winter, Season 1, 10.0, 30", "2, Summer, Season 2, 20.0, 45", "3, Spring, Season 3, 30.0, 60", "4, Autumn, Season 4, 40.0, 75"})
    void testGetAll(Long id, String name, String description, BigDecimal price, Integer duration) {
        Certificate certificate = Certificate.builder().id(id).name(name).description(description).price(price).duration(duration).build();
        List<Certificate> certificates = new ArrayList<>();

        certificates.add(certificate);

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(CertificateQueries.SELECT_ALL_WITH_TAGS, Certificate.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(certificates);

        List<Certificate> actualCertificates = certificateDao.getAll();

        assertEquals(certificates.size(), actualCertificates.size());

        IntStream.range(0, certificates.size()).forEach(i -> assertEquals(certificates.get(i), actualCertificates.get(i)));

        verify(entityManagerFactory).createEntityManager();
        verify(entityManager).createQuery(CertificateQueries.SELECT_ALL_WITH_TAGS, Certificate.class);

    }


    @DisplayName("Save certificate")
    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({"1, Winter, Season 1, 10.0, 30", "2, Summer, Season 2, 20.0, 45", "3, Spring, Season 3, 30.0, 60", "4, Autumn, Season 4, 40.0, 75"})
    void testSave(Long id, String name, String description, BigDecimal price, int duration) {

        Certificate certificate = Certificate.builder().id(id).name(name).description(description).price(price).duration(duration).build();

        Tag tag = new Tag();
        tag.setName("Seasonal");
        Set<Tag> tags = new HashSet<>();
        tags.add(tag);
        certificate.setTags(tags);

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);

        when(entityManager.createQuery("SELECT t FROM Tag t WHERE t.name = :name", Tag.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("name", tag.getName())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(tag));
        when(entityManager.merge(certificate)).thenReturn(certificate);

        Certificate actualCertificate = certificateDao.save(certificate);

        assertEquals(certificate, actualCertificate);

        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(entityManager).createQuery("SELECT t FROM Tag t WHERE t.name = :name", Tag.class);
        verify(typedQuery).setParameter("name", tag.getName());
        verify(typedQuery).getResultList();
        verify(entityManager).merge(certificate);
        verify(transaction).commit();
    }
}
