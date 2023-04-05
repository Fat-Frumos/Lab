package com.epam.esm;

import com.epam.esm.criteria.QueryBuilder;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.DefaultCertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.mapper.CertificateListExtractor;
import com.epam.esm.mapper.CertificateRowMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.stream.IntStream;

import static com.epam.esm.mapper.QueriesContext.UPDATE_CERTIFICATE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.util.AssertionErrors.fail;

class TestDefaultGiftCertificateDao {
    @Mock
    private static final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
    @Mock
    private static final CertificateRowMapper rowMapper = mock(CertificateRowMapper.class);
    @Mock
    private static final CertificateListExtractor listExtractor = mock(CertificateListExtractor.class);
    private static final CertificateDao certificateDao = new DefaultCertificateDao(jdbcTemplate, rowMapper, listExtractor);


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
}
