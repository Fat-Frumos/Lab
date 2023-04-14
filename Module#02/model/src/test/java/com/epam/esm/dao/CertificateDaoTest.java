package com.epam.esm.dao;

import com.epam.esm.criteria.QueryBuilder;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.mapper.CertificateListExtractor;
import com.epam.esm.mapper.CertificateRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.epam.esm.mapper.QueriesContext.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @DisplayName("Test delete certificate")
    void testDeleteCertificate() {
        long certificateId = 1L;
        doThrow(new RuntimeException("Certificate not found with id: " + certificateId))
                .when(jdbcTemplate).update(anyString(), anyLong());
        try {
            certificateDao.delete(certificateId);
            fail("Certificate not found with id: " + certificateId);
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), containsString(
                    "Certificate not found with id: " + certificateId));
        }
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
    void testSave() {
        when(jdbcTemplate.update(anyString(), anyLong(), anyString(), anyString(), anyDouble(), anyInt(), any(Instant.class), any(Instant.class)))
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

    @Test
    @DisplayName("Should return certificate with specified id")
    void testGetById() {
        when(jdbcTemplate.query(GET_CERTIFICATE_BY_ID, new Object[]{id}, listExtractor)).thenReturn(List.of(certificate));
        assertEquals(certificate, certificateDao.getById(id));
        verify(jdbcTemplate, times(1)).query(GET_CERTIFICATE_BY_ID, new Object[]{id}, listExtractor);
    }

    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid id")
    @MethodSource("provideInvalidIds")
    void testGetById_InvalidId(Long id) {
        assertThrows(CertificateNotFoundException.class, () -> certificateDao.getById(id));
    }
    static Stream<Arguments> provideInvalidIds() {
        return Stream.of(Arguments.of(-1L));
    }
}
