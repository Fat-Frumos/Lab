package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
class CertificateDaoTest {

    private CertificateDao certificateDao;
    @Mock
    private EntityManager entityManager;
    List<Certificate> certificates;

    @BeforeEach
    void setUp() {
        Certificate certificate1 = Certificate.builder()
                .id(1L)
                .name("Certificate 1")
                .description("Description 1")
                .price(BigDecimal.valueOf(10.0))
                .build();
        Certificate certificate2 = Certificate.builder()
                .id(2L)
                .name("Certificate 2")
                .description("Description 2")
                .price(BigDecimal.valueOf(20.0))
                .build();

        certificateDao = new CertificateDaoImpl(entityManager);
        certificates = Arrays.asList(certificate1, certificate2);
    }

    @Test
    @DisplayName("Get all certificates")
    void testGetAllCertificate() {


        TypedQuery<Certificate> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Certificate.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(certificates);

        List<Certificate> result = certificateDao.getAll();

        assertFalse(result.isEmpty());
        assertEquals(result, certificates);
        verify(entityManager).createQuery(anyString(), eq(Certificate.class));
    }

    @ParameterizedTest(name = "ID={0}")
    @DisplayName("Test find certificate by ID")
    @CsvSource({"1, Winter", "2, Summer", "3, Spring", "4, Autumn"})
    void testFindById(Long id, String name) {

        Certificate certificate = Certificate.builder()
                .id(id)
                .name(name)
                .description("Test certificate description")
                .price(BigDecimal.TEN)
                .duration(30)
                .createDate(Timestamp.valueOf(LocalDateTime.now()))
                .lastUpdateDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        when(entityManager.find(Certificate.class, id)).thenReturn(certificate);
        assertEquals(certificate, certificateDao.findById(id));

        verify(entityManager, times(1)).find(Certificate.class, id);
    }


    @ParameterizedTest
    @CsvSource({
            "1, 'Certificate 1', 'Description 1', 10.0",
            "2, 'Certificate 2', 'Description 2', 20.0"
    })
    @DisplayName("Get certificate by ID")
    void testGetById(long id, String name, String description, double price) {
        Certificate certificate = Certificate.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(BigDecimal.valueOf(price))
                .build();
        when(entityManager.find(Certificate.class, id)).thenReturn(certificate);
        Optional<Certificate> optionalCertificate = certificateDao.getById(id);

        assertTrue(optionalCertificate.isPresent());
        Certificate result = optionalCertificate.get();

        assertNotNull(result);
        assertEquals(result.getId(), (id));
        assertEquals(result.getName(), (name));
        assertEquals(result.getDescription(), (description));
        assertEquals(result.getPrice(), (BigDecimal.valueOf(price)));
        verify(entityManager).find(Certificate.class, id);
    }

    @Test
    void testGetAll() {
        List<Certificate> expectedCertificates = singletonList(
                Certificate.builder()
                        .id(1L)
                        .name("Certificate 1")
                        .description("Description 1")
                        .createDate(Timestamp.valueOf(LocalDateTime.now()))
                        .lastUpdateDate(Timestamp.valueOf(LocalDateTime.now()))
                        .duration(10)
                        .price(BigDecimal.valueOf(100.0))
                        .tags(singleton(
                                Tag.builder()
                                        .tagId(1L)
                                        .name("Tag 1")
                                        .build()
                        ))
                        .build()
        );

        TypedQuery<Certificate> query = Mockito.mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Certificate.class)))
                .thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(expectedCertificates);

        List<Certificate> actualCertificates = certificateDao.getAll();

        assertEquals(expectedCertificates.size(), actualCertificates.size());
        assertEquals(expectedCertificates.get(0).getId(), actualCertificates.get(0).getId());
        assertEquals(expectedCertificates.get(0).getName(), actualCertificates.get(0).getName());
        assertEquals(expectedCertificates.get(0).getDescription(), actualCertificates.get(0).getDescription());
        assertEquals(expectedCertificates.get(0).getCreateDate(), actualCertificates.get(0).getCreateDate());
        assertEquals(expectedCertificates.get(0).getLastUpdateDate(), actualCertificates.get(0).getLastUpdateDate());
        assertEquals(expectedCertificates.get(0).getDuration(), actualCertificates.get(0).getDuration());
        assertEquals(expectedCertificates.get(0).getPrice(), actualCertificates.get(0).getPrice());
        assertEquals(expectedCertificates.get(0).getTags(), actualCertificates.get(0).getTags());
    }
}
