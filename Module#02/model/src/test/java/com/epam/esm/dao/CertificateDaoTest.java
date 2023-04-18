package com.epam.esm.dao;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.QueryBuilder;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.mapper.CertificateListExtractor;
import com.epam.esm.mapper.CertificateRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.epam.esm.mapper.QueriesContext.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.fail;

class CertificateDaoTest {
    @Mock
    private static final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
    @Mock
    private static final CertificateRowMapper rowMapper = mock(CertificateRowMapper.class);
    @Mock
    private static final CertificateListExtractor listExtractor = mock(CertificateListExtractor.class);
    private static final CertificateDao certificateDao = new CertificateDaoImpl(jdbcTemplate, rowMapper, listExtractor);

    private Certificate certificate;
    private static final Long id = 1L;

    @BeforeEach
    public void setUp() {
        CertificateDto certificateDto =
                CertificateDto.builder()
                        .id(id)
                        .name("Gift")
                        .description("Test certificate description")
                        .price(BigDecimal.TEN)
                        .duration(30)
                        .build();

        certificate = Certificate.builder()
                .id(id)
                .name(certificateDto.getName())
                .description(certificateDto.getDescription())
                .price(certificateDto.getPrice())
                .duration(certificateDto.getDuration())
                .build();
    }

    @Test
    @DisplayName("Test Save certificate")
    void testSave() {
        when(jdbcTemplate.update(anyString(), anyLong(), anyString(),
                anyString(), anyDouble(), anyInt(),
                any(Instant.class), any(Instant.class)))
                .thenReturn(1);
        Long savedId = certificateDao.save(certificate);
        assertNotNull(savedId);
        verify(jdbcTemplate, times(1)).update(
                eq(INSERT_CERTIFICATE),
                eq(savedId),
                eq(certificate.getName()),
                eq(certificate.getDescription()),
                eq(certificate.getPrice()),
                eq(certificate.getDuration()),
                any(Timestamp.class),
                any(Timestamp.class));
    }

    @DisplayName("Test Certificate Update SQL Generation")
    @ParameterizedTest(name = "Run {index}: certificate = {0}, expected = {1}")
    @CsvSource({
            "name1,description1,10,50,1,0",
            "name2,,20,50,2,1",
            "name,,,0,3,2"
    })
    void testUpdateSqlGeneration(String name, String description, BigDecimal price, int duration, long id, int c) {
        String[] expected = {
                "UPDATE gift_certificates c SET c.name='name1', c.description='description1', c.price='10', c.duration='50', c.last_update_date='%s' WHERE c.id=1;",
                "UPDATE gift_certificates c SET c.name='name2', c.price='20', c.duration='50', c.last_update_date='%s' WHERE c.id=2;",
                "UPDATE gift_certificates c SET c.name='name', c.last_update_date='%s' WHERE c.id=3;"};

        Certificate certificate = Certificate.builder()
                .id(id)
                .name(name)
                .duration(duration)
                .description(description)
                .price(price)
                .build();

        certificateDao.update(certificate);
        String[] s = String.format(expected[c], Instant.now()).split(" ");
        String[] split = QueryBuilder.builder().updateQuery(certificate, UPDATE_CERTIFICATE).build().split(" ");
        IntStream.range(0, split.length).filter(i -> i != split.length - 3).forEach(i -> assertEquals(s[i], split[i]));
    }

    @Test
    @DisplayName("Should return certificate with specified id")
    void testGetById() {
        when(jdbcTemplate.query(GET_CERTIFICATE_BY_ID, new Object[]{id}, listExtractor))
                .thenReturn(List.of(certificate));
        assertEquals(certificate, certificateDao.getById(id).orElseThrow());
        verify(jdbcTemplate, times(1))
                .query(GET_CERTIFICATE_BY_ID, new Object[]{id}, listExtractor);
    }

    @ParameterizedTest
    @DisplayName("Should return certificate by name")
    @CsvSource({"test_certificate, 1"})
    void getByName_ShouldReturnCertificateByName(String name, Long id) {
        Certificate expectedCertificate = Certificate.builder().id(id).name(name).build();
        List<Certificate> certificates = Collections.singletonList(expectedCertificate);
        when(jdbcTemplate.query(String.format("%s'%s'", GET_CERTIFICATE_BY_NAME, name),
                rowMapper)).thenReturn(certificates);
        Optional<Certificate> actualCertificate = certificateDao.getByName(name);
        assertTrue(actualCertificate.isPresent());
        assertEquals(expectedCertificate, actualCertificate.get());
    }

    @Test
    @DisplayName("Get Certificate By Id with empty result")
    void getByIdReturnsEmptyOptional() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), eq(listExtractor)))
                .thenReturn(Collections.emptyList());
        Optional<Certificate> certificate = certificateDao.getById(id);
        assertTrue(certificate.isEmpty(), "Expected empty Optional");
    }

    @Test
    @DisplayName("Get Certificate By Name with empty result")
    void getByNameReturnsEmptyOptional() {
        String name = "test";
        when(jdbcTemplate.query(anyString(), eq(rowMapper)))
                .thenReturn(Collections.emptyList());
        Optional<Certificate> certificate = certificateDao.getByName(name);
        assertTrue(certificate.isEmpty());
    }

    @CsvSource({
            "name:test,description:test-desc,10,20,2023-04-01T00:00:00Z,2023-04-30T23:59:59Z",
            "name:test2,description:test-desc2,20,30,2023-05-01T00:00:00Z,2023-05-31T23:59:59Z"
    })
    @ParameterizedTest
    @DisplayName("should return a list of certificates matching the given criteria")
    void getAllByShouldReturnListOfCertificates(
            String name, String description, BigDecimal price, int duration,
            String createDateStr, String lastUpdateDateStr) {
        Criteria criteria = Criteria.builder()
                .name(name)
                .description(description)
                .date(Instant.parse(createDateStr))
                .build();

        Certificate certificate = Certificate.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(Instant.parse(createDateStr))
                .lastUpdateDate(Instant.parse(lastUpdateDateStr))
                .build();
        List<Certificate> certificates = Collections.singletonList(certificate);
        String query = QueryBuilder.builder().searchBy(criteria).build();
        when(jdbcTemplate.query(query, listExtractor)).thenReturn(certificates);
        List<Certificate> actualCertificate = certificateDao.getAllBy(criteria);
        assertFalse(actualCertificate.isEmpty());
        assertEquals(certificate, actualCertificate.get(0));
        assertEquals(certificates, actualCertificate);
        verify(jdbcTemplate, times(1)).query(query, listExtractor);
    }

    @Test
    @DisplayName("Test delete certificate")
    void testDeleteCertificate() {
        long certificateId = 1L;
        doThrow(new RuntimeException("Certificate not found with id: " + certificateId))
                .when(jdbcTemplate).update(anyString(), anyLong());
        try {
            certificateDao.delete(certificateId);
            fail("Certificate not found with id: " + certificateId);
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(),
                    "Certificate not found with id: " + certificateId);
        }
    }

    @DisplayName("delete() method should delete certificate and all references by id")
    @CsvSource({
            "1",
            "2",
            "3"
    })
    @ParameterizedTest
    void deleteShouldDeleteCertificateAndAllReferencesById(String id) {
        certificateDao.delete(Long.valueOf(id));
        verify(jdbcTemplate, times(1)).update(DELETE_REF, Long.valueOf(id));
        verify(jdbcTemplate, times(1)).update(DELETE_CERTIFICATE, Long.valueOf(id));
    }
}
